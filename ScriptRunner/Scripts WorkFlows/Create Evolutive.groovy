/**
Title: Creación Evolutive
Description: Script para la creación de Evolutivos a través de una iniciativa,
             creación de issue de tipo Task enlazada a la issue de tipo Initiative
Author: Alejandro Romero Juarros
Version:2.0 Cambio de Evolutivo como épica en vez de task
*/

//Definición de Variables
//Evolutivo como Épica
def issuetype = "10000"
def linkName = "Relates"
def issueKey = issue.key

//Obtener Información de la issue de tipo Iniciativa
def currentIssueInfo = get('/rest/api/2/issue/' + issueKey)
    .header('Content-Type', 'application/json')
    .asObject(Map)
    
def currentProjectKey = currentIssueInfo.body.fields.project.key

//Condición de ejecución del Script
//Debe ser una issue de tipo Initiative con clave PDP
if(currentProjectKey == "PDP") return true
else return false

//Obtener valor de scope
def scope = issue.fields.customfield_10046.value
def scopeChild = issue.fields.customfield_10046?.child?.value

//Formar project Name a través de la concatenación de scope + año actual
def now = new Date()
def year = now[Calendar.YEAR]
def projectName = "${scope} ${year}"

//Búsqueda de proyecto con el mismo nombre que el Project Name
def project = get("/rest/api/3/project/search")
    .header("Content-Type", "application/json")
    .queryString("query", projectName)
    .asObject(Map).body

if(!project?.values?.get(0)){
    return "No project found for issue ${issue.key}"
}

//Obtener componente con el mismo nombre que el scope
def component = get("/rest/api/3/project/${issue.fields.project.key}/component")
    .header("Content-Type", "application/json")
    .queryString("query", scope)
    .asObject(Map).body

def asssigneeAccountId = ""
if(component.total == 0){
    logger.warn "No se ha encontrado ningún componente para la issue ${issue.key}"
}else{
    asssigneeAccountId = component.values[0].assignee?.accountId
}

//Cuerpo de la descripción de la issue
def description
if(issue.fields.description instanceof Map){
    description = issue.fields.description
}else{
    description = [
        "type" : "doc",
        "version" : 1,
        "content" : [
            [
                "type" : "paragraph",
                "content" : [
                    [
                        "type" : "text",
                        "text" : issue.fields.description
                    ]
                ]
            ]
        ]
    ]
}

//Cuerpo de la issue de tipo Épica con todos los campos necesarios y sus respectivos valores
def body = [
    "fields": [
        "project": [
            "key": project.values.get(0).key
        ],
        "issuetype": [
            "id": issuetype
        ],
        "summary": issue.fields.summary,
        "description": description,
        "assignee": [
            "id": asssigneeAccountId
        ],
        "customfield_10046": issue.fields.customfield_10046, // Scope
        "customfield_10011": issue.fields.summary, //Epic Name
        "customfield_11152": issue.fields.customfield_11152 //Customer Date
    ]
]

//Creación de la issue a través de llamada POST REST API
def newIssue = post("/rest/api/3/issue")
    .header("Content-Type", "application/json")
    .body(body)
    .asObject(Map).body

//Cuerpo del issue link 
body = [
    "inwardIssue": [
        "key": issue.key
    ],
    "outwardIssue": [
        "key": newIssue.key
    ],
    "type": [
        "name": linkName
    ]
]

//Creación del issue link "Relates To" de la issue de tipo Iniciativa con la issue de tipo Épica
post("/rest/api/3/issueLink")
    .header("Content-Type", "application/json")
    .body(body)
    .asObject(Map).body

//Asignación de Responsable Project Manager (assignedPM) de la issue
def assignedPM = issue.fields.customfield_10050?.accountId
def assig
if(assignedPM){
    assig = assignedPM
} else {
    assig = null;
}

//Asignación de Responsable (assignee) de la issue
def evol= newIssue.key
if (assig) {
def updateProject = put("/rest/api/3/issue/" + evol + "/assignee" )
    .header("Content-Type", "application/json")
    .body( accountId: assig).asString()
}