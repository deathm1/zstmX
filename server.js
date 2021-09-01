//imports
const colors = require('colors');
console.clear();
console.log(" //-----------------------------------KoshurTech zstmX Server-----------------------------------// \n".bgRed.bold);




//imports
const express = require('express');
const dotenv = require('dotenv').config({ path: "./configuration/config.env" });
const db = require('./database/database');

//Intialize express server framework
console.log(getTime() + " : Launching Express Server Framework...");
const app = new express();
app.use(express.json());

//Create connection with database
db();

try {
    console.log(getTime() + " : Launching Routes...");

    // Route SET 1
    app.use('/', require('./routes/landingPage'));

    // Route SET 2
    app.use('/api/getIpInfo', require('./routes/getIpInfo'));

    // Route SET 3
    app.use('/api/uploadDeviceSpecs', require('./routes/uploadDeviceSpecs'));


    // Route SET 4
    app.use('/api/getDeviceSpecs', require('./routes/getDeviceSpecs'));

    console.log(getTime() + " : All routes launched successfully.".green.bold);
}
catch (error) {
    console.log(error);
    console.log(getTime() + " : Routes were not launched successfully.".red.bold);
}


app.listen(process.env.PORT, () => {
    console.log(getTime() + " : Server is running at port " + ` ${process.env.PORT} `.white.bold.bgRed);
});


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