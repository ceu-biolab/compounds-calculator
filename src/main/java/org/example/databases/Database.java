package org.example.databases;

import org.example.domain.FattyAcid;
import org.example.exceptions.FattyAcidCreation_Exception;
import org.example.exceptions.InvalidFormula_Exception;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class Database {
    private static Connection connection = null;
    private final String url = "jdbc:mysql://localhost:3306/compounds";
    private final String username = "root";
    private final String password = "schoenstatt37";

    public Database() {
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Set<FattyAcid> getFattyAcidFromDatabase(double fattyAcidMass) throws SQLException, InvalidFormula_Exception, FattyAcidCreation_Exception {
        String query = "SELECT chain_id, num_carbons, double_bonds FROM chains WHERE mass <= " + fattyAcidMass + " + 0.01 AND mass >= " + fattyAcidMass + " - 0.01 AND oxidation = ''";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();

        Set<FattyAcid> fattyAcids = new HashSet<>();

        while (resultSet.next()) {
            int carbonAtoms = resultSet.getInt("num_carbons");
            int doubleBonds = resultSet.getInt("double_bonds");
            fattyAcids.add(new FattyAcid(carbonAtoms, doubleBonds));
        }
        return fattyAcids;
    }
}

