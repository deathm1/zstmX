const colors = require('colors');
const express = require('express');
const dotenv = require('dotenv').config({ path: "./configuration/config.env" });
const router = express.Router();
const recaptcha = require('../middleware/recaptchaMiddleware');
const ipStats = require('../middleware/ipStatistics');

router.get('/', recaptcha, ipStats, async (req, res, next) => {
    try {
        console.log(getTime() + ' : Getting public IP information....'.bold);
        //console.log(req.data);
        if (req.ip && req.recaptcha) {
            res.status(200).json({
                success: true,
                isReCaptchaVerificationSuccess: true,
                ipInfo: req.data
            });
        }
        else {
            res.status(400).json({
                success: false,
                status: 'IP information was not generated'
            });
        }

    }
    catch (error) {
        console.log(error);
        console.log(getTime() + ' : 500 : SERVER ERROR : Something went wrong'.red.bold);
        res.status(500).json({ status: 'Something went wrong with recaptcha middleware', success: false });
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





