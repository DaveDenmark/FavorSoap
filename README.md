# FavorDrop - SOAP API
Live version: `52.213.91.0:18372/FavorDropSoap?wsdl`

For at benytte API’et kan man benytte konsol programmet eller JSP siden.

For at teste API’et kan SoapUI benyttes.
Da vi benytter et JWT til authentication, skal man først kalde login metoden som returnere din token, og alle andre requests vil efter kræve at du angiver din token som parameter.


# Setting up for local deployment
## Requirements
IDE: NetBeans

## Import the project 
Clone the repository and import to the IDE.

## Serving the API
Run `server.java`
