'use strict';
module.exports = function(app) {
    let user = require('../controllers/userController');

    // user Routes
    app.route('/users')
        .get(user.list_all_users)
        .post(user.create_a_user);

    app.route('/users/:userId')
        .get(user.read_a_user)
        .put(user.modify_a_user);

    app.route('/authentication')
        .get(user.authentication);
};
