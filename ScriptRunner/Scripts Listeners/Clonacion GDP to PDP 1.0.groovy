/**
Title: Clonacion GDP to PDP
Description: Script para clonar iniciativas del proyecto GDP al proyecto PDP,
             con el addon Exalate en lenguaje Groovy
Author: Alejandro Romero Juarros
Version:1.0
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
