const mongoose = require('mongoose');
const dotenv = require('dotenv').config({ path: "./configuration/config.env" });



//Async expression

const createConnectionWithDatabase = async () => {
    try {
        console.log(getTime() + " : Creating Connection with database...");

        await mongoose.connect(process.env.DATABASE_URI, {
            useNewUrlParser: true,
            useUnifiedTopology: true,
            useFindAndModify: false,
            useCreateIndex: true
        }).then(response => {
            //console.log(response);
            console.log(getTime() + " : (PROMISE RESPONSE) Database connection has been established.".green.bold);

        }).catch(err => {
            console.log(err);
            console.log(getTime() + " : (PROMISE RESPONSE) Database connection could not be established.".red.bold);
        });
    }
    catch (error) {
        console.log(getTime() + ' Something went wrong, Could not establish connection with database. Shutting down server.'.bold.red);
        console.log(err.message);

        // Exit Process with failure
        process.exit(1);
    }
};

module.exports = createConnectionWithDatabase;

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