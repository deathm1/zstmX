const colors = require("colors");
const express = require("express");
const recaptchaMiddleware = require("../middleware/recaptchaMiddleware");
const router = express.Router();
const Device = require("../database/models/deviceSpecification");
const { v4: uuidv4 } = require("uuid");

router.post("/", recaptchaMiddleware, async (req, res) => {
    try {
        if (!req.recaptcha) {
            console.log(
                getTime() +
                " : 400 : BAD REQUEST : reCaptcha verification was not completed.".red
                    .bold
            );
            res
                .status(400)
                .json({
                    status: "reCaptcha verification was not completed.",
                    success: false,
                });
        } else {
            const deviceModel = req.body.deviceModel;
            const deviceMake = req.body.deviceMake;
            const deviceInfo = req.body.deviceInfo;
            const deviceId = req.body.deviceId;

            if (
                deviceInfo == null ||
                deviceInfo == "" ||
                deviceMake == null ||
                deviceMake == "" ||
                deviceId == null ||
                deviceId == "" ||
                deviceModel == null ||
                deviceModel == ""
            ) {
                console.log(
                    getTime() + " : 400 : BAD REQUEST : Request data is missing.".red.bold
                );
                res
                    .status(400)
                    .json({ status: "Request data is missing.", success: false });
            } else {
                console.log(
                    getTime() + " : Adding device specifications to database...".bold
                );

                const find = await Device.findOne({ deviceId: deviceId });

                if (find == null) {
                    let device = new Device();
                    device.entryId = uuidv4();
                    device.deviceModel = deviceModel;
                    device.deviceMake = deviceMake;
                    device.deviceId = deviceId;
                    device.deviceInfo = deviceInfo;
                    await device
                        .save()
                        .then((response) => {
                            console.log(getTime() + " : 200 : OK : Device specifications have been uploaded.".green.bold);
                            res
                                .status(200)
                                .json({
                                    status: "Device specifications have been uploaded.",
                                    success: true,
                                });
                        })
                        .catch((error) => {
                            console.log(error);
                            console.log(getTime() + " : 500 : SERVER ERROR : Something went wrong while uploading data into database.".green.bold);
                            res
                                .status(500)
                                .json({
                                    status:
                                        "Something went wrong while uploading data into database.",
                                    success: false,
                                });
                        });
                }

                else {
                    await Device.findOne({ deviceId: deviceId }).updateMany({
                        deviceInfo: deviceInfo,
                        deviceMake: deviceMake,
                    }).then(response => {
                        console.log(getTime() + " : 200 : OK : This device has already made a device specification entry, Updating with new specifications.".green.bold);
                        res
                            .status(200)
                            .json({
                                status: "Device specifications have been updated.",
                                success: true,
                            });
                    }).catch(error => {
                        console.log(error);
                        console.log(getTime() + " : 500 : SERVER ERROR : Something went wrong while updating data into database.".green.bold);
                        res
                            .status(500)
                            .json({
                                status:
                                    "Something went wrong while updating data into database.",
                                success: false,
                            });
                    });
                }
            }
        }
    } catch (err) {
        console.log(err);
        console.log(
            getTime() +
            " : 500 : SERVER ERROR : Something went wrong with Route 3.".red.bold
        );
        res
            .status(500)
            .json({ status: "Something went wrong with Route 3.", success: false });
    }
});

router.post("/putUpVote", recaptchaMiddleware, async (req, res) => {
    try {
        if (!req.recaptcha) {
            console.log(
                getTime() +
                " : 400 : BAD REQUEST : reCaptcha verification was not completed.".red
                    .bold
            );
            res
                .status(400)
                .json({
                    status: "reCaptcha verification was not completed.",
                    success: false,
                });
        } else {
            const deviceId = req.body.deviceId;
            const eId = req.body.entryId;

            if (deviceId == null || deviceId == "" || eId == null || eId == "") {
                console.log(
                    getTime() + " : 400 : BAD REQUEST : Request data is missing.".red.bold
                );
                res
                    .status(400)
                    .json({ status: "Request data is missing.", success: false });
            } else {
                console.log(getTime() + " Adding upvote to database...".bold);
                const getPost = await Device.findOne({ entryId: eId });
                if (getPost != null) {
                    const getUpvoteArray = getPost.upVotes;
                    const getDownvoteArray = getPost.downVotes;
                    var isUpvoted = false;
                    for (var i = 0; i < getUpvoteArray.length; i++) {
                        if (getUpvoteArray[i].deviceId == deviceId) {
                            //Already Upvoted
                            isUpvoted = true;
                        }
                    }
                    if (isUpvoted) {
                        console.log(getTime() + " : 200 : OK : Already Upvoted".green.bold);
                        res
                            .status(200)
                            .json({ status: "Already Up voted", success: true })
                            .end();
                    } else {
                        const payload = {
                            upVoteId: uuidv4(),
                            deviceId: deviceId,
                        };
                        const post = await Device.findOne({ entryId: eId });
                        var array = post.upVotes;
                        array.push(payload);
                        const post2 = await Device.findOne({ entryId: eId }).updateMany({ "upVotes": array });
                        for (var i = 0; i < getDownvoteArray.length; i++) {
                            if (getDownvoteArray[i].deviceId == deviceId) {
                                await Device.updateMany(
                                    { entryId: eId },
                                    { $pull: { downVotes: { _id: getDownvoteArray[i]._id } } }
                                );
                            }
                        }

                        console.log(getTime() + " : 200 : OK : Up voted".green.bold);
                        res.status(200).json({ status: "Up voted", success: true });
                    }
                } else {
                    console.log(
                        getTime() +
                        " : 400 : BAD REQUEST : Device post was not found.".red.bold
                    );
                    res
                        .status(400)
                        .json({ status: "Device post was not found.", success: false });
                }
            }
        }
    } catch (err) {
        console.log(err);
        console.log(
            getTime() +
            " : 500 : SERVER ERROR : Something went wrong with Route 4.".red.bold
        );
        res
            .status(500)
            .json({ status: "Something went wrong with Route 4.", success: false });
    }
});

router.post("/putDownVote", recaptchaMiddleware, async (req, res) => {
    try {
        if (!req.recaptcha) {
            console.log(
                getTime() +
                " : 400 : BAD REQUEST : reCaptcha verification was not completed.".red
                    .bold
            );
            res
                .status(400)
                .json({
                    status: "reCaptcha verification was not completed.",
                    success: false,
                });
        } else {
            const deviceId = req.body.deviceId;
            const eId = req.body.entryId;

            if (deviceId == null || deviceId == "" || eId == null || eId == "") {
                console.log(
                    getTime() + " : 400 : BAD REQUEST : Request data is missing.".red.bold
                );
                res
                    .status(400)
                    .json({ status: "Request data is missing.", success: false });
            } else {
                console.log(getTime() + " Adding Down Vote to database...".bold);
                const getPost = await Device.findOne({ entryId: eId });
                if (getPost != null) {
                    const getUpvoteArray = getPost.upVotes;
                    const getDownvoteArray = getPost.downVotes;
                    var isDownvoted = false;
                    for (var i = 0; i < getDownvoteArray.length; i++) {
                        if (getDownvoteArray[i].deviceId == deviceId) {
                            //Already Upvoted
                            isDownvoted = true;
                        }
                    }

                    if (isDownvoted) {
                        console.log(
                            getTime() + " : 200 : OK : Already Down Voted".green.bold
                        );
                        res
                            .status(200)
                            .json({ status: "Already Down Voted", success: true })
                            .end();
                    } else {
                        const payload = {
                            downVoteId: uuidv4(),
                            deviceId: deviceId,
                        };

                        const post = await Device.findOne({ entryId: eId });
                        var array = post.downVotes;
                        array.push(payload);

                        const post2 = await Device.findOne({ entryId: eId }).updateMany({ "downVotes": array });

                        //await post.save();
                        for (var i = 0; i < getUpvoteArray.length; i++) {
                            if (getUpvoteArray[i].deviceId == deviceId) {
                                console.log("got");
                                await Device.updateMany(
                                    { entryId: eId },
                                    { $pull: { upVotes: { _id: getUpvoteArray[i]._id } } }
                                );
                            }
                        }

                        console.log(getTime() + " : 200 : OK : Down Voted".green.bold);
                        res.status(200).json({ status: "Down Voted", success: true });
                    }
                } else {
                    console.log(
                        getTime() +
                        " : 400 : BAD REQUEST : Device post was not found.".red.bold
                    );
                    res
                        .status(400)
                        .json({ status: "Device post was not found.", success: false });
                }
            }
        }
    } catch (err) {
        console.log(err);
        console.log(
            getTime() +
            " : 500 : SERVER ERROR : Something went wrong with Route 4.".red.bold
        );
        res
            .status(500)
            .json({ status: "Something went wrong with Route 4.", success: false });
    }
});

module.exports = router;

function getTime() {
    let date_ob = new Date();
    // current date
    // adjust 0 before single digit date
    let date = ("0" + date_ob.getDate()).slice(-2);
    // current month
    let month = ("0" + (date_ob.getMonth() + 1)).slice(-2);
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
    var out =
        year +
        "-" +
        month +
        "-" +
        date +
        " " +
        hours +
        ":" +
        minutes +
        ":" +
        seconds +
        ":" +
        mseconds;
    return out;
}
