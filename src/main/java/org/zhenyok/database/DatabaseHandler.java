package org.zhenyok.database;

import org.zhenyok.pojo.Group;
import org.zhenyok.pojo.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

public class DatabaseHandler extends Const {
    private Connection dbConnection;

    public static void main(String[] args) throws SQLException {
        DatabaseHandler handler = new DatabaseHandler();
        handler.getConnection();
    }

    public Connection getConnection() throws SQLException {
        if (dbConnection != null && !dbConnection.isClosed()) {
            return dbConnection;
        }

        Properties props = new Properties();
        props.setProperty("user", "postgres");
        props.setProperty("password", "Np!ge83E3iiTKGM");
        String url = "jdbc:postgresql://db.ieedqmagxkrupglowgom.supabase.co:5432/postgres"; //jdbc:postgresql://localhost:1488/ClientServer
        dbConnection = DriverManager.getConnection(url, props);
        return dbConnection;
    }

    public int createProduct(Product product) {
        String query = "INSERT INTO " + PRODUCTS_TABLE + "(name, count, price, group_id, characteristics, supplier) VALUES (?, ?, ?, "+(getGroupId(product.getGroup())==-1?null:getGroupId(product.getGroup()))+", ?, ?) RETURNING id";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, product.getName());
            statement.setInt(2, product.getCount());
            statement.setDouble(3, product.getPrice());
            statement.setString(4, product.getCharacteristic());
            statement.setString(5, product.getSupplier());

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("id");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public void createGroup(Group group) {
        String query = "INSERT INTO " + GROUPS_TABLE + "(name) VALUES (?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, group.getName());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getProductCount(String name) {
        String query = "SELECT count FROM " + PRODUCTS_TABLE + " WHERE name = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("count");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    public int getProductPrice(String name) {
        String query = "SELECT price FROM " + PRODUCTS_TABLE + " WHERE name = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("price");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    public boolean checkProductByName(String name) {
        String query = "SELECT COUNT(*) FROM " + PRODUCTS_TABLE + " WHERE name = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public Product getProduct(String name) {
        String query = "SELECT p.count, p.price, p.group_id, g.name, p.supplier, p.characteristic " +
                "FROM " + PRODUCTS_TABLE + " p LEFT JOIN " + GROUPS_TABLE + " g ON p.group_id = g.id " +
                "WHERE p.name = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt("count");
                    int price = resultSet.getInt("price");
                    int groupId = resultSet.getInt("group_id");
                    String groupName = resultSet.getString("name");
                    String supplier = resultSet.getString("supplier");
                    String characteristic = resultSet.getString("characteristic");

                    Group group = groupId == 0 ? null : new Group(groupName);
                    if (group != null) {
                        return new Product(0, name, count, price, group.getName(), supplier, characteristic);
                    }

                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public Product getProductById(int id) {
        String query = "SELECT p.name, p.count, p.price, p.group_id, g.name, p.supplier, p.characteristic " +
                "FROM " + PRODUCTS_TABLE + " p LEFT JOIN " + GROUPS_TABLE + " g ON p.group_id = g.id " +
                "WHERE p.id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String name = resultSet.getString("name");
                    int count = resultSet.getInt("count");
                    int price = resultSet.getInt("price");
                    int groupId = resultSet.getInt("group_id");
                    String groupName = resultSet.getString("name");
                    String supplier = resultSet.getString("supplier");
                    String characteristic = resultSet.getString("characteristic");

                    Group group = groupId == 0 ? null : new Group(groupName);
                    String groupNameFinal = group == null ? "" : group.getName();
                    return new Product(0, name, count, price, groupNameFinal, supplier, characteristic);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public boolean setName(int id, String name) {
        String query = "UPDATE " + PRODUCTS_TABLE + " SET name = ? WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            statement.setInt(2, id);
            int rows = statement.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean setCount(int id, int count) {
        String query = "UPDATE " + PRODUCTS_TABLE + " SET count = ? WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, count);
            statement.setInt(2, id);
            int rows = statement.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean checkGroupByName(String name) {
        String query = "SELECT COUNT(*) FROM " + GROUPS_TABLE + " WHERE name = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public String getGroup(int id) {
        String query = "SELECT name " + "FROM " + GROUPS_TABLE + " WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {

                    return resultSet.getString("name");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return "";
    }

    public int getGroupId(String name) {
        String query = "SELECT id " + "FROM " + GROUPS_TABLE + " WHERE name = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {

                    return resultSet.getInt("id");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return -1;
    }

    public String getGroupByName(String name) {
        String query = "SELECT name " + "FROM " + GROUPS_TABLE + " WHERE name = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {

                    return resultSet.getString("name");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return "";
    }

    public boolean setGroup(int id, String groupName) {
        String query = "UPDATE " + PRODUCTS_TABLE + " SET group_id = (SELECT id FROM " + GROUPS_TABLE + " WHERE name = ?) WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, groupName);
            statement.setInt(2, id);
            int rows = statement.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean setPrice(int id, double price) {
        String query = "UPDATE " + PRODUCTS_TABLE + " SET price = ? WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDouble(1, price);
            statement.setInt(2, id);
            int rows = statement.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean removeProduct(String name) {
        String query = "DELETE FROM " + PRODUCTS_TABLE + " WHERE name = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            int rows = statement.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean removeGroup(String name) {
        String query = "DELETE FROM " + GROUPS_TABLE + " WHERE name = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            int rows = statement.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getGroupByProdName(String name) {
        String query = "SELECT g.name FROM " + GROUPS_TABLE + " g JOIN " + PRODUCTS_TABLE + " p ON g.id = group_id WHERE p.name = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                return set.getString(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public ArrayList<Product> sort(String sortingCriteria) {
        String query = "SELECT id, name, count, price, group_id, supplier, characteristic FROM " + PRODUCTS_TABLE + " ORDER BY " + sortingCriteria;
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet set = statement.executeQuery();
            ArrayList<Product> products = new ArrayList<>();
            while (set.next()) {
                int id = set.getInt("id");
                String productName = set.getString("name");
                String supplier = set.getString("supplier");
                String characteristic = set.getString("characteristic");
                int count = set.getInt("count");
                double price = set.getDouble("price");
                int groupId = set.getInt("group_id");
                Product product = new Product(id, productName, count, price, getGroup(groupId), supplier, characteristic);
                products.add(product);
            }
            return products;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Group> sortCategories(String sortingCriteria) {
        String query = "SELECT name FROM " + GROUPS_TABLE + " ORDER BY " + sortingCriteria;
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet set = statement.executeQuery();
            ArrayList<Group> products = new ArrayList<>();
            while (set.next()) {
                String productName = set.getString("name");
                Group product = new Group(productName);
                products.add(product);
            }
            return products;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}




