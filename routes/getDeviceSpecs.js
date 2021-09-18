const colors = require('colors');
const { query } = require('express');
const express = require('express');
const router = express.Router();
const Device = require('../database/models/deviceSpecification');
const dotenv = require('dotenv').config({ path: "./configuration/config.env" });

// router.get('/', async (req, res) => {
//     console.clear();
//     console.log(getTime() + ' : Getting all profiles with limit...'.bold);

//     try {
//         var limit = req.body.limit;
//         if (limit == null || limit == 0) {
//             limit = 10;
//         }
//         const queryArray = await Device.find().limit(limit)
//                 .then(response => {
//                     console.log(getTime() + ' : 200 : OK : Results were fetched successfully.'.green.bold);
//                     res.status(200).json({
//                         success: true,
//                         results: response
//                     });
//                 })
//                 .catch(error => {
//                     console.log(error)
//                     console.log(getTime() + ' : 500 : SERVER ERROR : Something went wrong.'.green.bold);
//                     res.status(500).json({
//                         success: false,
//                         status: 'Something went wrong.'
//                     });
//                 });
//     }
//     catch (error) {
//         console.log(error);
//         console.log(getTime() + ' : 500 : SERVER ERROR : Something went wrong.'.green.bold);
//         res.status(500).json({
//             success: false,
//             status: 'Something went wrong.'
//         });
//     }
// });


router.get('/getProfileByText', async (req, res) => {
    console.clear();
    try {
        const query = req.body.textQuery;
        if (query == null || query == "") {
            console.log(getTime() + ' : 400 : BAD REQUEST : Can not search database without query.'.red.bold);
            res.status(400).json({ status: 'Can not search database without query.', success: false });
        }
        else {


            const queryArray = await Device.aggregate(
                [
                    { $addFields: { 
                            resultObject: { 
                                $regexFind: { 
                                    input: "$deviceModel", 
                                    regex: query 
                                } 
                            } 
                        } 
                    },
                    {
                        $project:{
                            serverTime:1, 
                            entryId:1, 
                            upVotes:1, 
                            downVotes:1, 
                            deviceModel:1,
                            resultObject:1,
                            deviceMake:1,
                            _id:0
                        }
                    }
                ])
                .then(response => {
                    console.log(getTime() + ' : 200 : OK : Results were fetched successfully.'.green.bold);

                    const resArr = response;
                    const saveArr = [];
                    for(var i=0; i<resArr.length; i++){
                        var obj = {
                            "serverTime":resArr[i].serverTime,
                            "upVotes":resArr[i].upVotes.length,
                            "downVotes":resArr[i].downVotes.length,
                            "entryId":resArr[i].entryId,
                            "deviceModel":resArr[i].deviceModel,
                            "deviceMake":resArr[i].deviceMake,
                            "resultObject":resArr[i].resultObject
                        }
                        saveArr.push(obj);
                        
                    }
                    res.status(200).json({
                        success: true,
                        results: saveArr
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
            success: false,
            status: 'Something went wrong.'
        });
    }
});



router.get('/getProfileById', async (req, res) => {
    console.clear();
    console.log(getTime() + ' : Getting all profile by entry ID...'.bold);
    try {
        const id = req.body.id;
        if (id == null || id == "") {
            console.log(getTime() + ' : 400 : BAD REQUEST : Can not search database without id.'.red.bold);
            res.status(400).json({ status: 'Can not search database without id.', success: false });
        }
        else {



            const response = await Device.findOne({entryId:id})

            if(response!=null){
                console.log(getTime() + ' : 200 : OK : Results were fetched successfully.'.green.bold);
                    res.status(200).json({
                        success: true,
                        "serverTime":response.serverTime,
                        "upVotes":response.upVotes.length,
                        "downVotes":response.downVotes.length,
                        "entryId":response.entryId,
                        "deviceModel":response.deviceModel,
                        "deviceMake":response.deviceMake,
                        "deviceInfo":response.deviceInfo
                    });
                }
            else{
                console.log(getTime() + ' : 404 : NOT FOUND : Profile not found.'.green.bold);
                res.status(404).json({
                    success: false,
                    status: 'Profile not found.'
                });
            }
            
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



//get devices by make and model ggwp

router.get('/makeModel', async (req, res) => {
    console.clear();
    console.log(getTime() + ' : Getting all profiles with limit...'.bold);

    try {
        var limit = req.body.limit;
        if (limit == null || limit == 0) {
            limit = 10;
        }
        const queryArray = await Device.find({}, {serverTime:1, entryId:1, upVotes:1, downVotes:1, deviceModel:1, deviceMake:1,_id:0}).limit(limit)
                .then(response => {
                    console.log(getTime() + ' : 200 : OK : Results were fetched successfully.'.green.bold);


                    const resArr = response;
                    const saveArr = [];
                    for(var i=0; i<resArr.length; i++){
                        var obj = {
                            "serverTime":resArr[i].serverTime,
                            "upVotes":resArr[i].upVotes.length,
                            "downVotes":resArr[i].downVotes.length,
                            "entryId":resArr[i].entryId,
                            "deviceModel":resArr[i].deviceModel,
                            "deviceMake":resArr[i].deviceMake
                        }
                        saveArr.push(obj);
                        
                    }

                    res.status(200).json({
                        success: true,
                        results: saveArr
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
    catch (error) {
        console.log(error);
        console.log(getTime() + ' : 500 : SERVER ERROR : Something went wrong.'.green.bold);
        res.status(500).json({
            success: false,
            status: 'Something went wrong.'
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


