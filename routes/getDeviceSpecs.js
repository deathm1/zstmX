const colors = require('colors');
const { query } = require('express');
const express = require('express');
const router = express.Router();
const Device = require('../database/models/deviceSpecification');
const dotenv = require('dotenv').config({ path: "./configuration/config.env" });

router.get('/', async (req, res) => {
    console.clear();
    console.log(getTime() + ' : Getting all profiles with limit...'.bold);

    try {
        var limit = req.body.limit;
        if (limit == null || limit == 0) {
            limit = 10;
        }
        else {

            const queryArray = await Device.find().limit(limit)
                .then(response => {
                    console.log(getTime() + ' : 200 : OK : Results were fetched successfully.'.green.bold);
                    res.status(200).json({
                        success: true,
                        results: response
                    });
                })
                .catch(error => {
                    console.log(error)
                    console.log(getTime() + ' : 500 : SERVER ERROR : Something went wrong.'.green.bold);
                    res.status(500).json({
                        success: false,
                        status: 'Something went wrong.'
                    });
                });
        }
    }
    catch (error) {
        console.log(error);
        console.log(getTime() + ' : 500 : SERVER ERROR : Something went wrong.'.green.bold);
        res.status(500).json({
            success: false,
            status: 'Something went wrong.'
        });
    }
});



router.get('/getProfileByText', async (req, res) => {
    try {
        const query = req.body.textQuery;
        if (query == null || query == "") {
            console.log(getTime() + ' : 400 : BAD REQUEST : Can not search database without query.'.red.bold);
            res.status(400).json({ status: 'Can not search database without query.', success: false });
        }
        else {


            const queryArray = await Device.aggregate([{ $addFields: { resultObject: { $regexFind: { input: "$deviceModel", regex: query } } } }])
                .then(response => {
                    console.log(getTime() + ' : 200 : OK : Results were fetched successfully.'.green.bold);
                    res.status(200).json({
                        success: true,
                        results: response
                    });
                }).catch(error => {
                    console.log(error)
                    console.log(getTime() + ' : 500 : SERVER ERROR : Something went wrong.'.green.bold);
                    res.status(500).json({
                        success: false,
                        status: 'Something went wrong.'
                    });
                });
        }

    }
    catch (error) {
        console.log(error);
        console.log(getTime() + ' : 500 : SERVER ERROR : Something went wrong.'.green.bold);
        res.status(200).json({
            success: false
        });
    }
});

module.exports = router;


function getTime() {
    let date_ob = new Date();
    // current date
    // adjust 0 before single digit date
    let date = ("0" + date_ob.getDate()).slice(-2);
    // current month
    let month = ("0" + (
        date_ob.getMonth() + 1
    )).slice(-2);
    // current year
    let year = date_ob.getFullYear();
    // current hours
    let hours = date_ob.getHours();
    // current minutes
    let minutes = date_ob.getMinutes();
    // current seconds
    let seconds = date_ob.getSeconds();
    // current seconds
    let mseconds = date_ob.getMilliseconds();
    var out = year + "-" + month + "-" + date + " " + hours + ":" + minutes + ":" + seconds + ":" + mseconds;
    return out;
}


