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

    app.route('/authentication/:email_address/:encrypted_password')
        .get(user.authentication);

    app.route('/countries_list')
        .get(user.country_list);

    app.route('/languages_list')
        .get(user.language_list);

    app.route('/film_types')
        .get(user.film_types);

    app.route('/cities_of_country/:country')
        .get(user.cities_of_country);

    app.route('/matches/:id')
        .put(user.match)
};
