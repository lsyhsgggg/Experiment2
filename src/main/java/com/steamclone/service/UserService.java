package com.steamclone.service;

import com.steamclone.dao.UserDAO;
import com.steamclone.entity.User;

public class UserService {
    private UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    // 用户登录 - 使用简单密码验证
    public User login(String username, String password) {
        User user = userDAO.findByUsername(username);
        if (user != null && password.equals(user.getPassword())) {
            return user;
        }
        return null;
    }

    // 用户注册 - 直接保存密码，不做哈希处理
    public boolean register(String username, String password, String email) {
        // 检查用户名是否已存在
        if (userDAO.findByUsername(username) != null) {
            return false;
        }

        // 检查邮箱是否已存在
        if (userDAO.findByEmail(email) != null) {
            return false;
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password); // 直接保存明文密码
        user.setEmail(email);
        user.setAvatarUrl("images/avatars/default_avatar.jpg");

        return userDAO.addUser(user);
    }

    // 验证用户名是否可用
    public boolean isUsernameAvailable(String username) {
        return userDAO.findByUsername(username) == null;
    }

    // 验证邮箱是否可用
    public boolean isEmailAvailable(String email) {
        return userDAO.findByEmail(email) == null;
    }

    // 更新用户信息
    public boolean updateUser(User user, String newPassword) {
        if (newPassword != null && !newPassword.isEmpty()) {
            user.setPassword(newPassword); // 直接使用新密码
        }
        return userDAO.updateUser(user);
    }

    // 获取用户信息
    public User getUserById(int userId) {
        return userDAO.findById(userId);
    }
}