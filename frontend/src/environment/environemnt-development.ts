export const environment = {
    production: false,
    API_GATEWAY: "http://localhost:8222",
    keycloak: {
        realm: 'larana-microservices-realm',
        clientId: 'angular-client',
        url: 'http://localhost:8181'
    },
    PRODUCT_BASEURL: "http://localhost:8081",
    CART_API: "http://localhost:8082",
    USER_API: "http://localhost:8083",
    PRODUCT_API: "http://localhost:8084",
};