/**
Title: ProjectInfo
Description: Script para rellenar los campos del project-info (Hoja Resumen) de cada proyecto
Author: Alejandro Romero Juarros
Version:1.0 
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