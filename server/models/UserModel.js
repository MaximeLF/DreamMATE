
'use strict';
const mongoose = require('mongoose');
const Schema = mongoose.Schema;
const country_List = require('country-list');
const language_List = require('langs');
const countries = country_List.getNames();
const languages = language_List.names();
const film_types = ['action', 'romantic', 'comedy','science fiction','documentary','thriller','horror','adventure','historical drama'];

let UserSchema = new Schema({
    first_name: {
        type: String,
    },
    last_name: {
        type: String,
    },
    email_address: {
       type: String,
        required: true
    },
    encrypted_password : {
      type: String,
        required: true
    },
    birth_date:{
        type: Date,
    },
    gender:{
        type: {
            type: String,
            enum: ['male', 'female']
        }
    },
    origin_country:{
        type: {
            type: String,
            enum: countries
        },
    },
    languages_spoken:{
        type: [{
            type: String,
            enum: languages
        }],
    },
    Created_date: {
        type: Date,
        default: Date.now
    },
    film_type: {
        type: [{
            type: String,
            enum: film_types
        }],
    },
    min_budget: {
        type: Number,
    },
    max_budget: {
        type: Number,
    },
    arrival_date:{
        type: Date,
    },
    departure_date:{
        type: Date,
    },
    smoker:{
        type : Boolean,
    },
    like_pets:{
        type : Boolean,
    },
    sleep_time:{
        type: {
            type: String,
            enum: ['9:00-10:00','10:00-12:00','12:00+']
        },
    }
});

module.exports = mongoose.model('Users', UserSchema);
exports.film_type = film_types;