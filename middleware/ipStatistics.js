const axios = require('axios');
const requestIp = require('request-ip');
const dotenv = require('dotenv').config({ path: "./configuration/config.env" });



module.exports = async function (req, res, next) {

    try {
        const Ip = requestIp.getClientIp(req);
        var url = 'http://ip-api.com/json/' + Ip + '?fields=status,message,continent,continentCode,country,countryCode,region,regionName,city,district,zip,lat,lon,timezone,offset,currency,isp,org,as,asname,reverse,mobile,proxy,hosting,query'
        await axios.get(url).then(async function (response) {
            req.data = response.data;
            console.log(getTime() + ' : 200 : OK : IP info was generated.'.green.bold);
            req.ip = true;
            next();
        }).catch(function (error) {
            req.ip = false;
            console.log(getTime() + ' : 400 : BAD REQUEST : IP address was invalid.'.red.bold);
            res.status(400).json({ success: false, status: '400 : BAD REQUEST : IP address was invalid.' }).end();
        });
    }
    catch (error) {
        console.log(error);
        console.log(getTime() + ' : 500 : SERVER ERROR : Something went wrong with IP middleware'.red.bold);
        res.status(500).json({ status: 'Server Error', success: false });
    }
};



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