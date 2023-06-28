package org.zhenyok.server;

import com.sun.net.httpserver.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.json.JSONArray;
import org.json.JSONObject;
import org.zhenyok.database.DatabaseHandler;
import org.zhenyok.pojo.Group;
import org.zhenyok.pojo.Product;

import java.io.*;
import java.net.InetSocketAddress;
import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.UUID;

import static org.zhenyok.server.MyServer.MyHandler.getBody;

public class MyServer {
    private static final int STATUS_OK = 200;
    private static final int STATUS_CREATED = 201;
    private static final int STATUS_NOT_FOUND = 404;
    private static final int STATUS_CONFLICT = 409;
    private static final int STATUS_NO_CONTENT = 204;
    private static final int STATUS_UNAUTHORIZED = 401;
    private static final int STATUS_FORBIDDEN = 403;

    private static final Key JWT_KEY = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);
    private static final String AUTH_TOKEN_PREFIX = "Bearer ";

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        HttpContext products = server.createContext("/", new MyHandler());
        HttpContext categories = server.createContext("/api/categories", new CategoryHandler());
//        HttpContext login = server.createContext("/login", new LoginHandler());

//        products.setAuthenticator(new Auth());
        server.setExecutor(null);
        server.start();
    }

    static class CategoryHandler implements HttpHandler {
        DatabaseHandler db = new DatabaseHandler();

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
                exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE");
                exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type, Authorization");
                exchange.sendResponseHeaders(204, -1); // Send a successful empty response
                exchange.close();
                return;
            }

            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE");
            exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type, Authorization");
            String path = exchange.getRequestURI().getPath();
            if (path.startsWith("/api/categories")) {
                String method = exchange.getRequestMethod().toLowerCase();
                if (method.equals("post")) {
                    System.out.println("updating");
                } else if (method.equals("get")) {
                    handleGetRequestCategory(path, exchange);
                } else if (method.equals("options")) {
                    handlePutRequestCategory(path, exchange);
                } else if (method.equals("delete")) {
                    handleDeleteRequestCategory(path,exchange);
                }
            } else {
                System.out.println("Invalid path");
            }
        }

        private void handleDeleteRequestCategory(String path, HttpExchange exchange) throws IOException {
            String name = (path.split("/")[3]); // correct value

        }

        public void handleGetRequestCategory(String path, HttpExchange exchange) throws IOException {
            ArrayList<Group> categories = db.sortCategories("name");
            System.out.println(categories);
            if (categories == null) {
                sendResponse("Category not found", STATUS_NOT_FOUND, exchange);
            } else {
                JSONArray jo = new JSONArray(categories);
                sendResponse(jo.toString(1), STATUS_OK, exchange);
            }
        }

        private void handlePutRequestCategory(String path, HttpExchange exchange) throws IOException {
            System.out.println("In here");
            String values = getBody(exchange);
            JSONObject jsonBody = new JSONObject(values);
            String name = jsonBody.getString("name");
            Group group = new Group(name);
            db.createGroup(group);
            sendResponse("", STATUS_NO_CONTENT, exchange);
        }
    }

    static class MyHandler implements HttpHandler {
        DatabaseHandler db = new DatabaseHandler();

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
                exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE");
                exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type, Authorization");
                exchange.sendResponseHeaders(204, -1); // Send a successful empty response
                exchange.close();
                return;
            }

            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE");
            exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type, Authorization");

            String path = exchange.getRequestURI().getPath();
            if (path.startsWith("/api/good")) {
                String method = exchange.getRequestMethod().toLowerCase();
                if (method.equals("get")) {
                    handleGetRequest(path, exchange);
                } else if (method.equals("put")) {
                    handlePutRequest(exchange);
                } else if (method.equals("post")) {
                    handlePostRequest(path, exchange);
                } else if (method.equals("delete")) {
                    handleDeleteRequest(path, exchange);
                }
            } else {
                System.out.println("Invalid path");
            }
        }


        private void handleGetRequest(String path, HttpExchange exchange) throws IOException {
            String id = path.split("/")[3];
            ArrayList<Product> products = db.sort("name");
            System.out.println(products);
            if (products == null) {
                sendResponse("Product not found", STATUS_NOT_FOUND, exchange);
            } else {
                JSONArray jo = new JSONArray(products);
                sendResponse(jo.toString(1), STATUS_OK, exchange);
            }
        }


        private void handlePutRequest(HttpExchange exchange) throws IOException {
            String values = getBody(exchange);
            JSONObject jsonBody = new JSONObject(values);
            String name = jsonBody.getString("name");
            int quantity = jsonBody.getInt("quantity");
            double price = jsonBody.getDouble("price");
            if (getNonNullValuesLength(jsonBody) == 3) {
                if (!db.checkProductByName(name)) {
                    Product product = new Product(0,name, quantity, price, null);
                    int productId = db.createProduct(product);
                    sendResponse(name + " " + productId + " created successfully ", STATUS_CREATED, exchange);
                } else {
                    sendResponse("Product already exists", STATUS_CONFLICT, exchange);
                }
            }
        }

        private void handlePostRequest(String path, HttpExchange exchange) throws IOException {
            int id = Integer.parseInt(path.split("/")[3]);
            String values = getBody(exchange);
            JSONObject jsonBody = new JSONObject(values);
            String name = jsonBody.getString("name");
            int quantity = jsonBody.getInt("quantity");
            double price = jsonBody.getDouble("price");
            String groupName = jsonBody.getString("group");
            Product product = db.getProductById(id);
            if (product == null) {
                sendResponse("Product not found", STATUS_NOT_FOUND, exchange);
            } else if (quantity < -1 || price < -1) {
                sendResponse("Bad values", STATUS_CONFLICT, exchange);
            } else {
                if (!name.isEmpty()) {
                    db.setName(id, name);
                }
                if (quantity != -1) {
                    db.setCount(id, quantity);
                }
                if (price != -1) {
                    db.setPrice(id, price);
                }
                if (!groupName.isEmpty()) {
                    db.setGroup(id, groupName);
                }
                sendResponse("", STATUS_NO_CONTENT, exchange);
            }
        }

        private void handleDeleteRequest(String path, HttpExchange exchange) throws IOException {
            int id = Integer.parseInt(path.split("/")[3]);
            Product product = db.getProductById(id);
            if (product == null) {
                sendResponse("Product not found", STATUS_NOT_FOUND, exchange);
            } else {
                db.removeProduct(product.getName());
                sendResponse("", STATUS_NO_CONTENT, exchange);
            }
        }

        private void sendResponse(String str, int statusCode, HttpExchange exchange) throws IOException {
            exchange.sendResponseHeaders(statusCode, str.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(str.getBytes());
            os.close();
        }

        public static String getBody(HttpExchange exchange) throws IOException {
            try (InputStream requestBodyStream = exchange.getRequestBody();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(requestBodyStream))) {
                StringBuilder requestBody = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    requestBody.append(line);
                }

                return requestBody.toString();
            }
        }

        public static int getNonNullValuesLength(JSONObject json) {
            int nonNullValuesLength = 0;

            Iterator<String> keys = json.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                Object value = json.get(key);

                if (value != null && value != JSONObject.NULL) {
                    nonNullValuesLength++;
                }
            }

            return nonNullValuesLength;
        }
    }

    static class LoginHandler implements HttpHandler {
        private static final String USERNAME = "admin";
        private static final String PASSWORD = "admin123";

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (exchange.getRequestURI().getPath().startsWith("/login")) {
                String body = getBody(exchange);
                JSONObject jsonBody = new JSONObject(body);

                String login = jsonBody.getString("login");
                String password = jsonBody.getString("password");
                if (login.equals(USERNAME) && password.equals(PASSWORD)) {
                    String token = generateToken(login);
                    JSONObject responseJson = new JSONObject();
                    responseJson.put("token", token);

                    sendResponse(responseJson.toString(), STATUS_OK, exchange);
                } else {
                    sendResponse("Unauthorized", STATUS_UNAUTHORIZED, exchange);
                }
            } else {
                sendResponse("Method not allowed", STATUS_FORBIDDEN, exchange);
            }
        }

        public static String generateToken(String login) {
            String id = UUID.randomUUID().toString().replace("-", "");

            Date now = new Date();
            Date exp = new Date(System.currentTimeMillis() + (1000 * 3000));

            String token;
            try {
                token = Jwts.builder()
                        .setId(id)
                        .setSubject(login)
                        .setIssuedAt(now)
                        .setNotBefore(now)
                        .setExpiration(exp)
                        .signWith(JWT_KEY)
                        .compact();
            } catch (Exception e) {
                e.printStackTrace();
                token = id;
            }

            return token;
        }
    }

    private static void sendResponse(String str, int statusCode, HttpExchange exchange) throws IOException {
        try {
            exchange.sendResponseHeaders(statusCode, str.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(str.getBytes());
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class Auth extends Authenticator {
        @Override
        public Result authenticate(HttpExchange httpExchange) {
            String authorizationHeader = httpExchange.getRequestHeaders().getFirst("Authorization");

            if (authorizationHeader != null && authorizationHeader.startsWith(AUTH_TOKEN_PREFIX)) {
                String token = authorizationHeader.substring(AUTH_TOKEN_PREFIX.length());

                try {
                    Claims claims = Jwts.parserBuilder()
                            .setSigningKey(JWT_KEY)
                            .build()
                            .parseClaimsJws(token)
                            .getBody();

                    String login = claims.getSubject();
                    return new Success(new HttpPrincipal(login, "realm"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return new Failure(STATUS_UNAUTHORIZED);
        }
    }
}

