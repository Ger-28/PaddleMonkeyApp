
'use strict';

const express = require('express');
const router = express.Router();
const reservasService = require('./reservas-service');

router.get("/",(req, res) => {
    reservasService.getAll((err, reservas) => {
        if (err) {
            res.status(500).send({
                msg: err,
            });
        } else if (reservas.length == 0) {
            res.status(500).send({
                msg: "No existen reservas registradas",
            });
        } else {
            res.status(200).send(reservas);
        }
    });
});

router.get("/:id", (req, res) => {
    const id = req.params.id;

    reservasService.get(id, (err, reserva) => {
        if (err) {
            res.status(500).send({
                msg: err,
            });
        } else if (reserva.length === 0) {
            res.status(404).send({ msg: "Reserva no encontrada" });
        } else {
            res.status(200).send(reserva);
        }
    });
});

router.post("/", (req, res) => {
    const reserva = req.body;

    reservasService.add(reserva, (err, reserva) => {
        if (err) {
            res.status(500).send({
                msg: err,
            });
        } else if (reserva.length != 0) {
            res.status(201).send({ msg: "Reserva registrada exitosamente" });
        }
    });
});

router.put("/:id", (req, res) => {
    const id = req.params.id;
    const reservaActualizada = req.body;

    reservasService.update(id, reservaActualizada, (err, numUpdates) => {
        if (err) {
            res.status(500).msg({ msg: err });
        } else if (numUpdates.modifiedCount === 0) {
            res.status(500).send({
                msg: "No actualizada"
            })
        } else {
            res.status(200).send({
                msg: "Reserva actualizada"
            });
        }
    });
});

router.delete("/", (req, res) => {
    reservasService.removeAll((err) => {
        if (err) {
            res.status(500).send({
                msg: err,
            });
        } else {
            res.status(200).send({
                msg: "Reservas eliminadas"
            })
        }
    })
});

router.delete("/:_id", function(req, res){
    const _id = req.params._id;
    reservasService.remove(_id, (err) => {
        if (err) {
            res.status(500).send({
                msg: err
            });
        } else {
            res.status(200).send({
                msg: "Reserva eliminada exitosamente"
            })
        }
    })
});


module.exports = router;