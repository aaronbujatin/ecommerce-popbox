export const environment = {
    production: false,
    API_GATEWAY: "http://localhost:8222",
    keycloak: {
        realm: 'larana-microservices-realm',
        clientId: 'angular-client',
        url: 'http://localhost:8181'
    }
};