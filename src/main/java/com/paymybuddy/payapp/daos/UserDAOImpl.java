package com.paymybuddy.payapp.daos;

import com.paymybuddy.payapp.constants.DBStatements;
import com.paymybuddy.payapp.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import javax.validation.constraints.Email;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class UserDAOImpl implements UserDAO {

    private final DataSource datasource;

    @Autowired
    public UserDAOImpl(DataSource datasource) {
        this.datasource = datasource;
    }

    @Override
    public User findById(int id) {
        return null;
    }

    @Override
    public User findByMail(@Email String mail) throws SQLException {
        //TODO ajouter la validation de l'email
        Connection con = datasource.getConnection();
        PreparedStatement ps = con.prepareStatement(DBStatements.GET_USER_BY_MAIL);
        ps.setString(1, mail);
        ResultSet rs = ps.executeQuery();
        User user = new User(rs.getInt("id"))
                .withUsername(rs.getString("username"))
                .withMail(rs.getString("mail"))
                .withPassword(rs.getString("password"));
        // TODO ajouter les roles (vérifier la requete SQL nécessaire)

        rs.close();
        ps.close();
        con.close();

        return user;
    }

    @Override
    public User findByUsername(String username) {
        return null;
    }

    public DataSource getDatasource() {
        return datasource;
    }
}
