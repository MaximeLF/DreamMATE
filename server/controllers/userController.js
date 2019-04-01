const mongoose = require('mongoose'),
    User = mongoose.model('Users');
const connectToDatabase = require('../db');
require('dotenv').config({ path: './variables.env' });

exports.list_all_users = function(req, res) {
    connectToDatabase().then(()=>{
        User.find({}, function(err, user) {
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
        User.find({email_address:req.body.email, encrypted_password:req.body.password},'_id first_name last_name',function (err, user){
            if(err)
                res.send(err);
            res.json(user);
        });
    });
};