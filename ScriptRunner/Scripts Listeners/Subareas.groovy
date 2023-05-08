/**
Title: Subareas
Description: Script para la asignación de responsables por subárea en vez de directores de area,
y bajar a un nivel jerárquico más bajo la decisión y gestión de iniciativas
Version:2.0 Eliminación nombres de responsables en comentario por privacidad
*/

// Definition of variables
def issueKey = issue.key
def id

sleep(7000)
//Get information about the issue
def currentIssueInfo = get('/rest/api/2/issue/' + issueKey)
    .header('Content-Type', 'application/json')
    .asObject(Map)    

def scopeChild= currentIssueInfo.body.fields.customfield_10046.child.value
 switch(scopeChild) {            
        case "Security and IT Infrastructures":
            id="62e0f3b93aaeedcae756150d"
            break;
        case "Deployments":
            id="62e0edee5b8cc57412052efb"
            break;
        case "SAP Basis":
            id="611f8bdf41ff95006aacbdd5"
            break;
        case "Engineering Solutions":
            id="62e0eaf04b574e9f2cafae9b"
            break;
        case "User Workplace & Databases":
            id="611f8bdf41ff95006aacbdd5"
            break;
        case "India Applications":
            id="611f8bdf41ff95006aacbdd5"
            break;
        case "EDI Support":
            id="62e0fb4d2fe585febb3bdbf9"
            break;
        case "SAP Support":
            id="62e0ef003257defaf50d20bb"
            break;
        case "MES Support":
            id="62e0fb4d2fe585febb3bdbf9"
            break;
        case "Helpdesk":
            id="625e5f901046bb0071dd596e"
            break;
        case "India Helpdesk":
            id="625e5f901046bb0071dd596e"
            break;
        case "HQ Support":
            id="625e5f901046bb0071dd596e"
            break;
        case "Finances": 
            id="62e0ec102fe585febb3bd873"
            break;
        case "GRC": 
            id="62e0ec102fe585febb3bd873"
            break;
        case "B2B Projects": 
            id="62e0eb0ab6b0b70770d80cb4"
            break;
        case "SAP Projects": 
            id="62e0eaedf6dd8b8b0eabb52d"
            break;
        case "MES Projects": 
            id="62e0eaa33257defaf50d1f97"
            break;
        case "Corporate Processes": 
            id="62e0eb2c4913d0b7348933fc"
            break;
        case "Industrial Projects": 
            id="62e0eb2c4913d0b7348933fc"
            break;
        case "Advanced Manufacturing": 
            id="6111158675ad9600697c5aab"
            break;
        case "Europe, Africa, India & Mercosur": 
            id="619e541cebce4700671f5f0a"
            break;
        case "NAFTA": 
            id="619e541cebce4700671f5f0a"
            break;
        case "ASIA": 
            id="619e541cebce4700671f5f0a"
            break;
        case "PMO": 
            id="619e5402b43d5b006a068b47"
            break;
        case "Information Security System": 
            id="62e0eb6d8cc02d0b34bd7180"
            break;
        case "BeOne & Development": 
            id="62e0e9cb3257defaf50d1f4b"
            break;
        case "SAP Development": 
            id="62e0e9cb3257defaf50d1f4b"
            break;
        case "Business Intelligence": 
            id="62e0f2e64913d0b7348935f9"
            break;
        case "Human Resources":
            id="62e0ec102fe585febb3bd873"
              break;
      }

/* Cuerpo para actualizar la issue con el responsable correspondiente de cada subarea */
def updateProject = put("/rest/api/2/issue/" + issueKey + "/assignee" )
    .header("Content-Type", "application/json")
    .body(accountId: id).asString()