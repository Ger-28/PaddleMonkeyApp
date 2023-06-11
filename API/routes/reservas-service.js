
'use strict';
const MongoClient = require('mongodb').MongoClient;
let db;
let ObjectId = require('mongodb').ObjectId;
const Reservas = function () {};


Reservas.prototype.connectDb = function (callback) {
    MongoClient.connect("mongodb+srv://Monkey:Monkey@cluster0.cgumky6.mongodb.net/?retryWrites=true&w=majority",
        {useNewUrlParser: true, useUnifiedTopology: true},
        function (err, database) {
            if (err) {
		        console.log(err);
                callback(err);
            }

   	    db = database.db('Monkey').collection('reservas');
	    console.log("Conexión correcta");

            callback(err, database);
        });
};

Reservas.prototype.add = (reserva, callback) => {
    return db.insertOne(reserva, callback);
};

Reservas.prototype.get = (_id, callback) => {
    return db.find({ _id: ObjectId(_id) }).toArray(callback);
};

Reservas.prototype.getAll = function (callback) {
    return db.find({}).toArray(callback);
};

Reservas.prototype.update = (_id, reservaActualizada, callback) => {
    delete reservaActualizada._id;
    return db.updateOne(
        { _id: ObjectId(_id) },
        { $set: reservaActualizada },
        callback
    );
};

Reservas.prototype.remove = function(_id, callback){
    return db.deleteOne({ _id: ObjectId(_id) }, callback);
};
Reservas.prototype.removeAll = (callback) => db.deleteMany({}, callback);

module.exports = new Reservas();