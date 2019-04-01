const mongoose = require('mongoose'),
    User = mongoose.model('Users');
const connectToDatabase = require('../db');
require('dotenv').config({ path: './variables.env' });
const userModel = require('../models/UserModel');
const country_List = require('country-list');
const language_List = require('langs');
//TODO afficher des logs des requêtes
//TODO faire une route pour les pays et les langues

exports.list_all_users = function(req, res) {
    connectToDatabase().then(()=>{
        User.find({},'first_name last_name', function(err, user) {
            if (err)
                res.send(err);
            res.json(user);
        });
    });
};
exports.create_a_user = function(req, res) {
    connectToDatabase().then(()=>{
        let new_user = new User(req.body);
        new_user.save(function(err, user) {
            if (err)
                res.send(err);
            res.json(user);
        });
    });
};
exports.modify_a_user = function(req, res){
    connectToDatabase().then(()=>{
        User.findOneAndUpdate({_id: req.params.userId}, req.body, {new: true}, function(err, user) {
            if (err)
                res.send(err);
            res.json(user);
        });
    });
};
exports.read_a_user = function(req, res){
    connectToDatabase().then(()=>{
        User.findById(req.params.userId, function(err, user) {
            if (err)
                res.send(err);
            res.json(user);
        });
    });
};

exports.authentication = function (req, res) {
    connectToDatabase().then(()=>{
        User.find({email_address : req.params.email_address, encrypted_password : req.params.encrypted_password},'_id first_name last_name',function (err, user){
            if(err){
                res.send(err);
            }
            if(user.length===0) {
                res.json('-1');
            }else {
                res.json(user);
            }
        });
    });
};

exports.country_list = function(req, res) {
    res.json(country_List.getNames());
};

exports.language_list = function (req, res) {
    res.json(language_List.names());
};

exports.film_types = (req,res)=>res.json(userModel.film_type);