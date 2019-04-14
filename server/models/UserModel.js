
'use strict';
const mongoose = require('mongoose');
const Schema = mongoose.Schema;
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
    telephone: {
        type: String
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
            enum: ['male', 'female','other']
        }
    },
    staying_city:{
        type: String,
    },
    staying_country:{
        type: String,
    },
    origin_countries:{
        type: [String],
    },
    languages_spoken:{
        type: [String],
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
    },
    matches:{
        type: [Schema.ObjectId]
    },
    occupation:{
        type:{
            type: String,
            enum: ['Local student', 'International student', 'Worker','Other']
        }
    },
    description:{
        type: String,
    },
    music:{
        type: String,
    },
    sports:{
        type:String,
    },
    avatar:{
        type: Number
    }
});

module.exports = mongoose.model('Users', UserSchema);