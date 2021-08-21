const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const deviceSpecification = new mongoose.Schema({
    serverTime: {
        type: String,
        default: convertTZ(new Date(), "Asia/Kolkata")
    },
    entryId: {
        required: true,
        type: String
    },
    deviceMake: {
        required: true,
        type: String
    },
    deviceModel: {
        required: true,
        type: String
    },
    deviceInfo: {
    },
    upVotes: [{
        upVoteId: {
            type: String,
            required: true
        },
        deviceId: {
            type: String,
            required: true
        },
        date: {
            type: Date,
            default: convertTZ(new Date(), "Asia/Kolkata")
        }
    }],
    downVotes: [{
        downVoteId: {
            type: String,
            required: true
        },
        deviceId: {
            type: String,
            required: true
        },
        date: {
            type: Date,
            default: convertTZ(new Date(), "Asia/Kolkata")
        }
    }]
});


function convertTZ(date, tzString) {
    return new Date((typeof date === "string" ? new Date(date) : date).toLocaleString("en-US", { timeZone: tzString }));
}

module.exports = mongoose.model('DeviceSpecification', deviceSpecification);

