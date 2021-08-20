const colors = require('colors');
const express = require('express');
const dotenv = require('dotenv').config({ path: "./configuration/config.env" });
const router = express.Router();




router.get('/', async (req, res) => {
    console.clear();
    console.log(getTime() + " : zstmX landing page has been accessed.");

    res.status(200).json({
        status: 'Landing Page has been accessed',
        success: true
    });
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
