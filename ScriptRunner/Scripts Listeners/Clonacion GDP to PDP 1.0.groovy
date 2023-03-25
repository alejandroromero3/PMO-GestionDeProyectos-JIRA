/**
Title: Clonacion GDP to PDP
Description: Script para clonar iniciativas del proyecto GDP al proyecto PDP,
             con el addon Exalate en lenguaje Groovy
Author: Alejandro Romero Juarros
Version:2.0 Inclusión campos multicascada desplegables y clonación bidireccional GDP to PDP y viceversa
*/

//Definición de Variables campos simples
def issuetype
def status
def summary
def description
def reporter
def attachments
def strategic_objective
def confidential
def PMO_Email

//Definición de Variables campos cascada
def scopeValue
def requesterAreaValue
def scopeChildValue
def requesterAreaChildValue
def scope
def requesterArea
def scopeChild
def requesterAreaChild
def requester
def customerDate

//Igualación campos simples GDP to PDP
GDP.issue.issuetype = PDP.issue.issuetype
GDP.issue.status = PDP.issue.status
GDP.issue.summary = PDP.issue.issuetype
GDP.issue.description = PDP.issue.description
GDP.issue.reporter = PDP.issue.reporter
GDP.issue.attachments = PDP.issue.attachments
//Strategic Objective
GDP.issue.customfield_10053 = PDP.issue.customfield_10053
//Confidential
GDP.issue.customfield_10092 = PDP.issue.customfield_10092
//PMO Email
GDP.issue.customfield_11167 = PDP.issue.customfield_11167

/*Igualar valor de los campos multicascada (desplegables) a variables
  Direccion GDP to PDP, para ser bidireccional
*/
//Campo Requester
requester = GDP.issue.customFields."11165"?.id
//Campo Scope 
scopeValue = GDP.issue.customFields."10046"?.value?.parent?.value
//Campo Requester Area
requesterAreaValue = GDP.issue.customFields."11162"?.value?.parent?.value
//Campo Customer Date
customerDate = GDP.issue.customFields."11152".value
//Campo Scope valor hijo
scopeChildValue = GDP.issue.customFields."10046"?.value?.child?.value
//Campo Requester valor hijo
requesterAreaChildValue = GDP.issue.customFields."11162"?.value?.child?.value
//Opcion campo Scope
scope = nodeHelper.getOption(GDP.issue, 10046L, scopeValue)
//Opcion campo Requester Area
requesterArea = nodeHelper.getOption(GDP.issue, 11162L, requesterAreaValue)

//Condicion campo Scope dependiendo del valor
scopeChild = scope?.childOptions?.find{ it.value == scopeChildValue }
if(scope){
    PDP.issue.customFields."10046".value = nodeHelper.getCascadingSelect(scope, scopeChild)
}else{
    PDP.issue.customFields."10046".value =  null
}

//Condicion campo requester area dependiendo del valor
requesterAreaChild = requesterArea?.childOptions?.find{ it.value == requesterAreaChildValue }
if(requesterArea){
    PDP.issue.customFields."11162".value = nodeHelper.getCascadingSelect(requesterArea, requesterAreaChild)
}else{
    PDP.issue.customFields."11162".value =  null
}

//Condicion campo requester dependiendo del valor
if(requester){
    PDP.issue.customFields."11165".id = requester
}else{
    PDP.issue.customFields."11165".id =  null
}

/*Igualar valor de los campos multicascada (desplegables) a variables
  Direccion PDP to GDP, para ser bidireccional
*/
//Campo Requester
requester = PDP.issue.customFields."11165"?.id
//Campo Scope 
scopeValue = PDP.issue.customFields."10046"?.value?.parent?.value
//Campo Requester Area
requesterAreaValue = PDP.issue.customFields."11162"?.value?.parent?.value
//Campo Customer Date
customerDate = PDP.issue.customFields."11152".value
//Campo Scope valor hijo
scopeChildValue = PDP.issue.customFields."10046"?.value?.child?.value
//Campo Requester valor hijo
requesterAreaChildValue = PDP.issue.customFields."11162"?.value?.child?.value
//Opcion campo Scope
scope = nodeHelper.getOption(PDP.issue, 10046L, scopeValue)
//Opcion campo Requester Area
requesterArea = nodeHelper.getOption(PDP.issue, 11162L, requesterAreaValue)

//Condicion campo Scope dependiendo del valor
scopeChild = scope?.childOptions?.find{ it.value == scopeChildValue }
if(scope){
    GDP.issue.customFields."10046".value = nodeHelper.getCascadingSelect(scope, scopeChild)
}else{
    GDP.issue.customFields."10046".value =  null
}

//Condicion campo requester area dependiendo del valor
requesterAreaChild = requesterArea?.childOptions?.find{ it.value == requesterAreaChildValue }
if(requesterArea){
    GDP.issue.customFields."11162".value = nodeHelper.getCascadingSelect(requesterArea, requesterAreaChild)
}else{
    GDP.issue.customFields."11162".value =  null
}

//Condicion campo requester dependiendo del valor
if(requester){
    PDP.issue.customFields."11165".id = requester
}else{
    PDP.issue.customFields."11165".id =  null
}
