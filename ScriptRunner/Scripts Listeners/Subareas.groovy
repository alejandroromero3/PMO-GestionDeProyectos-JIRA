/**
Title: Subareas
Description: Script para la asignación de responsables por subárea en vez de directores de area,
y bajar a un nivel jerárquico más bajo la decisión y gestión de iniciativas
Version:1.0
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
//return scopeChild
 switch(scopeChild) {            
        case "Security and IT Infrastructures":
            //Manuel Sanchez
            id="62e0f3b93aaeedcae756150d"
            break;
        case "Deployments":
            //Sergio Alegre
            id="62e0edee5b8cc57412052efb"
            break;
        case "SAP Basis":
            //Ivan Perez
            id="611f8bdf41ff95006aacbdd5"
            break;
        case "Engineering Solutions":
            //Javier Perez
            id="62e0eaf04b574e9f2cafae9b"
            break;
        case "User Workplace & Databases":
            //Ivan Perez
            id="611f8bdf41ff95006aacbdd5"
            break;
        case "India Applications":
            //Ivan Perez
            id="611f8bdf41ff95006aacbdd5"
            break;
        case "EDI Support":
            //Oscar Luis Gomez
            id="62e0fb4d2fe585febb3bdbf9"
            break;
        case "SAP Support":
            //Carmen Ares
            id="62e0ef003257defaf50d20bb"
            break;
        case "MES Support":
            //Oscar Luis Gomez
            id="62e0fb4d2fe585febb3bdbf9"
            break;
        case "Helpdesk":
            //Catalina Garcia
            id="625e5f901046bb0071dd596e"
            break;
        case "India Helpdesk":
            //Catalina Garcia
            id="625e5f901046bb0071dd596e"
            break;
        case "HQ Support":
            //Catalina Garcia
            id="625e5f901046bb0071dd596e"
            break;
        case "Finances": 
            //María García
            id="62e0ec102fe585febb3bd873"
            break;
        case "GRC": 
            //María García
            id="62e0ec102fe585febb3bd873"
            break;
        case "B2B Projects": 
            //Oscar Olivares
            id="62e0eb0ab6b0b70770d80cb4"
            break;
        case "SAP Projects": 
            //Pablo Marin
            id="62e0eaedf6dd8b8b0eabb52d"
            break;
        case "MES Projects": 
            //Donato Salas
            id="62e0eaa33257defaf50d1f97"
            break;
        case "Corporate Processes": 
            //Arturo Sagredo
            id="62e0eb2c4913d0b7348933fc"
            break;
        case "Industrial Projects": 
            //Arturo Sagredo
            id="62e0eb2c4913d0b7348933fc"
            break;
        case "Advanced Manufacturing": 
            //Laura Barquin
            id="6111158675ad9600697c5aab"
            break;
        case "Europe, Africa, India & Mercosur": 
            //Miguel Angel Izquierdo
            id="619e541cebce4700671f5f0a"
            break;
        case "NAFTA": 
            //Miguel Angel Izquierdo
            id="619e541cebce4700671f5f0a"
            break;
        case "ASIA": 
            //Miguel Angel Izquierdo
            id="619e541cebce4700671f5f0a"
            break;
        case "PMO": 
            //Beatriz Martin
            id="619e5402b43d5b006a068b47"
            break;
        case "Information Security System": 
            //Jose Luis Lozano
            id="62e0eb6d8cc02d0b34bd7180"
            break;
        case "BeOne & Development": 
            //Rodrigo Garcia
            id="62e0e9cb3257defaf50d1f4b"
            break;
        case "SAP Development": 
            //Rodrigo Garcia
            id="62e0e9cb3257defaf50d1f4b"
            break;
        case "Business Intelligence": 
            //Cesar Arribas
            id="62e0f2e64913d0b7348935f9"
            break;
        case "Human Resources":
            //María García
            id="62e0ec102fe585febb3bd873"
              break;
      }


def updateProject = put("/rest/api/2/issue/" + issueKey + "/assignee" )
    .header("Content-Type", "application/json")
    .body(accountId: id).asString()