/**
Title: ProjectInfo
Description: Script para rellenar los campos del project-info (Hoja Resumen) de cada proyecto.
Añadida funcionalidad para incluir y sincronizar miembros de ProjectTeam, dependiendo si son internos o externos
adquieran un rol de proyecto u otro. Condición y comparación con grupos de Azure/Directorio Activo 
para asegurar visibilidad y permisos en JIRA
Author: Alejandro Romero Juarros
Version:3.0 Mejora descripción
*/

// Definitions
def issueKey = issue.key

//Conditions
issue.issueType.name=="Project Info"

//Get information about the issue
def currentIssueInfo = get('/rest/api/2/issue/' + issueKey)
    .header('Content-Type', 'application/json')
    .asObject(Map)

def keyP = currentIssueInfo.body.fields.project.key

/* PRUEBA BUSQUEDA INCIDENCIA CUMPLA EL REQUISITO DE LA MISMA CLAVE  */
def busqProj = get("/rest/api/3/search")
    .header("Content-Type","application/json")
    .queryString("query", keyP)
    .asObject(Map).body
    
    
// Full info of parent request
def resultParentIssue = get('/rest/api/2/issue/' + busqProj)
	.header('Content-Type', 'application/json')
    .asObject(Map)


// Key of the parent request
//String parentKey = currentIssueInfo.body.fields.project.key

def projectKey = resultParentIssue.body.fields.customfield_10583
def projectLeader = resultParentIssue.body.fields.customfield_10585
def projectName = resultParentIssue.body.fields.customfield_10582

def body = [
    "fields": [
        "customfield_10585": projectLeader, //Project Leader
        "customfield_10582": projectName, //Project Name
        "customfield_10053": resultParentIssue.body.fields.customfield_10053, // Strategic Objective
        "customfield_10092": resultParentIssue.body.fields.customfield_10092, //Confidential
        "duedate": resultParentIssue.body.fields.duedate, //DueDate
        "customfield_10046": resultParentIssue.body.fields.customfield_10046 //Scope
    ]
]

if( keyP == projectKey){
def Project = put("/rest/api/3/issue/" + issueKey)
    .header("Content-Type", "application/json")
    .body(body)
    .asString()
}

def areabody = resultParentIssue.body.fields.customfield_10046.value
def confidentialbody = resultParentIssue.body.fields.customfield_10092.value
def documentationbody = resultParentIssue.body.fields.customfield_11081.value 
def projectLeaderbody = projectLeader.accountId
def areamanagerbody= resultParentIssue.body.fields.customfield_11104?.accountId
def subareareamanagerbody = resultParentIssue.body.fields.customfield_11202?.accountId
def projectTeambody = projectTeam?.accountId


//ROLES
def projleadPeople = put("https://antolin.atlassian.net/rest/api/2/project/"+keyP+"/role/10028")
    .header("Content-Type", "application/json;odata=verbose")
    .header("Accept", "application/json;odata=verbose")
    .body("categorisedActors": ["atlassian-user-role-actor": [projectLeaderbody] ])
    .asString()


def projArea = put("https://antolin.atlassian.net/rest/api/2/project/"+keyP+"/role/10029")
    .header("Content-Type", "application/json;odata=verbose")
    .header("Accept", "application/json;odata=verbose")
    .body("categorisedActors": ["atlassian-user-role-actor": [areamanagerbody]])
    .asString()


def projSubArea = put("https://antolin.atlassian.net/rest/api/2/project/"+keyP+"/role/10037")
    .header("Content-Type", "application/json;odata=verbose")
    .header("Accept", "application/json;odata=verbose")
    .body("categorisedActors": ["atlassian-user-role-actor": [subareareamanagerbody]])
    .asString()

if(projectTeambody){
    
    for(int i = 0; i < projectTeambody.size(); i++){
    
        def gruposProjTeam = get('https://antolin.atlassian.net/rest/api/3/user/groups?accountId='+ projectTeambody[i])
        .header("Accept", "application/json;odata=verbose")
        .asObject(List)
        
        if (gruposProjTeam.body.groupId.find{"7444b896-de52-497b-8c35-b704d51f1585"}){ //GroupId 
        
            def projteamPeople = post("https://antolin.atlassian.net/rest/api/2/project/"+keyP+"/role/10030") //Project Team para Visualización Completa 
            .header("Content-Type", "application/json;odata=verbose")
            .header("Accept", "application/json;odata=verbose")
            .body("user": [projectTeambody[i]])
            .asString()
            
            
        }else if (gruposProjTeam.body.groupId.find{"5fcaa7d9-96b3-4bc7-b626-41c0f6ba57f8"}){ //GroupId
            
            def projteamPeople = post("https://antolin.atlassian.net/rest/api/2/project/"+keyP+"/role/10035") //External Users para Visualización Restringida
            .header("Content-Type", "application/json;odata=verbose")
            .header("Accept", "application/json;odata=verbose")
            .body("user": [projectTeambody[i]])
        .asString()
        
        }
        
    }
    
    
    def body2 = [
        "name": "${projectName}", 
        "key": "${projectKey}",
        "area": "${areabody}", 
        "confidential": "${confidentialbody}", 
        "documentation": "${documentationbody}",
        "project team": "${projectTeambody}",
        "project leader": "${projectLeaderbody}",
        "area manager": "${areamanagerbody}"
    ]
    
    def PowerAutomate = post("https://prod-146.westeurope.logic.azure.com:443/workflows/c946dadcd15d478981d76250ea5ac580/triggers/manual/paths/invoke?api-version=2016-06-01&sp=%2Ftriggers%2Fmanual%2Frun&sv=1.0&sig=4muPj7VdencMPGidOAs5pk8TWM9WfkuhTujPquL9Ouc")
    .header("Content-Type", "application/json")
    .body(body2)
    .asString()
    
}else{
    def body3 = [
        "name": "${projectName}", 
        "key": "${projectKey}",
        "area": "${areabody}", 
        "confidential": "${confidentialbody}", 
        "documentation": "${documentationbody}",
        "project team": "${projectTeambody}",
        "project leader": "${projectLeaderbody}",
        "area manager": "${areamanagerbody}"
    ]
    
    def PowerAutomate = post("https://prod-146.westeurope.logic.azure.com:443/workflows/c946dadcd15d478981d76250ea5ac580/triggers/manual/paths/invoke?api-version=2016-06-01&sp=%2Ftriggers%2Fmanual%2Frun&sv=1.0&sig=4muPj7VdencMPGidOAs5pk8TWM9WfkuhTujPquL9Ouc")
    .header("Content-Type", "application/json")
    .body(body3)
    .asString()
}