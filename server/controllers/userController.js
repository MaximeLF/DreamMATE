const mongoose = require('mongoose'),
    User = mongoose.model('Users');
const connectToDatabase = require('../db');
require('dotenv').config({ path: './variables.env' });
const userModel = require('../models/UserModel');
const country_List = require('country-list');
const language_List = require('langs');
const cities = require('full-countries-cities');
const film_types = ['action', 'romantic', 'comedy','science fiction','documentary','thriller','horror','adventure','historical drama'];


exports.list_all_users = function(req, res) {
    connectToDatabase().then(()=>{
        User.find({},'first_name last_name ', function(err, users) {
            if (err){
                res.send(err);
                res.status(err.status || 500);
            }

            res.json(users);
        });
    });
};
exports.create_a_user = function(req, res) {
    connectToDatabase().then(()=>{
        let new_user = new User(req.body);
        new_user.save(function(err, user) {
            if (err){
                res.status(err.status || 500);
                res.send(err);
            }
            res.json(user);
        });
    });
};
exports.modify_a_user = function(req, res){
    connectToDatabase().then(()=>{
        User.findOneAndUpdate({_id: req.params.userId}, req.body, err=> {
            if (err){
                res.status(err.status || 500);
                res.send(err);
            }
            res.json("");
        });
    });
};
exports.read_a_user = function(req, res){
    connectToDatabase().then(()=>{
        User.findById(req.params.userId,{encrypted_password:0}, (err, user)=> {
            if (err){
                res.status(err.status || 500);
                res.send(err);
            }

            res.json(user);
        });
    });
};

exports.authentication = function (req, res) {
    connectToDatabase().then(()=>{
        User.find({email_address : req.params.email_address, encrypted_password : req.params.encrypted_password},'_id first_name last_name gender',function (err, user){
            if(err){
                res.status(err.status || 500);
                res.send(err);
            }
            if(user.length===0) {
                res.json({'_id':'','first_name':'','last_name':''});
            }else {
                res.json(user[0]);
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

exports.film_types = (req,res)=>{
    res.json(film_types);
};

exports.cities_of_country = (req,res)=>{
    const country = req.params.country;
    res.json(cities.getCities(country));
};

exports.match = (req,res) =>{
    getExistingMatches(req.params.id).then((arr)=>{
        if(arr[0]===true){
            connectToDatabase().then(()=>{
                let resTab=[];
                let tabPromise=[];
                arr[1].forEach(userId=>{
                    tabPromise.push(new Promise(resolve=>{
                        User.findById(userId,{encrypted_password:0},(err,u)=>{
                            if(err){
                                res.status(err.status || 500);
                                res.send(err);
                            }
                            resTab.push(u);
                            resolve();
                        });
                    }))

                });
                Promise.all(tabPromise).then(()=>{
                    calculateMatches(req.params.id);
                    res.json(resTab);
                });

            });
        }else{
            calculateMatches(req.params.id).then(arr=>res.json(arr)).catch(err=>{
                res.status(err.status || 500);
                res.send(err);
            });
        }
    }).catch(err=>{
        res.status(err.status || 500);
        res.send(err);
    });

};

function calculateMatches(id){
    return new Promise((resolve,reject)=>{
        connectToDatabase().then(()=>{

            User.find({},{encrypted_password:0},(err,users)=>{
                if(err){
                    reject(err);
                }
                const user1 = users.find(u=> u.id===id);
                const i = users.findIndex(u=> u.id===id);
                users.splice(i,1);
                let coefUsers = [];
                users.forEach(user=>{
                    if(cityMatch(user1,user)!==0){
                        let coef =dateMatchCoef(user1,user)+budgetMatchCoef(user1,user)+sleepTimeCoef(user1,user)+languageSpokenCoef(user1,user);
                        coefUsers.push([user,coef]);
                    }

                });
                coefUsers.sort((arr1,arr2)=>  arr2[1]-arr1[1]);
                coefUsers = coefUsers.slice(0,10);
                let finalTab = [];
                let finalTabToStore = [];
                coefUsers.forEach(element=>finalTab.push(element[0]));
                finalTab.forEach(element=>finalTabToStore.push(element._id));
                User.findOneAndUpdate({_id: id}, {matches:finalTabToStore});
                resolve(finalTab);


            });
        })
    });

}

function getExistingMatches(id){
    return new Promise((resolve,reject)=>{
        connectToDatabase().then(()=>{
            User.findById(id,(err,user)=>{
                if (err) reject(err);
                if(!user.matches || user.matches.length<1){
                    return resolve([false]);
                }else{
                    return resolve([true,user.matches]);
                }
            })
        })
    });
}

function dateMatchCoef(user1,user2){
    if(!user1.arrival_date || !user2.arrival_date) return 0;
    let res =0;
    if(user1.arrival_date.getMonth()===user2.arrival_date.getMonth() && user1.arrival_date.getFullYear()===user2.arrival_date.getFullYear()) res+= 20;

    if(user1.departure_date && user2.departure_date && user1.departure_date.getMonth()===user2.departure_date.getMonth() ) res+=10;

    return res;

}

function budgetMatchCoef(user1,user2){
    if(!user1.max_budget || !user2.max_budget ) return 0;
    const maxBudgetDelta = user1.max_budget*0.1;
    if(user2.max_budget<user1.max_budget+maxBudgetDelta ){
        return 20;
    }
    return 0;

}

function cityMatch(user1,user2){
    if(!user1.staying_city || !user2.staying_city) return 0;
    if(user1.staying_city===user2.staying_city) return 1;
    return 0;
}

function sleepTimeCoef(user1,user2){
    if(!user1.sleep_time || !user2.sleep_time) return 0;
    if(user1.sleep_time===user2.sleep_time) return 2;
    return 0;
}

function languageSpokenCoef(user1,user2){
    if(!user1.languages_spoken || !user2.languages_spoken) return 0;
    let res =0;
    user2.languages_spoken.forEach(lang=>{
        if(user1.languages_spoken.includes(lang)) res+=5;
    });
    return res;
}





