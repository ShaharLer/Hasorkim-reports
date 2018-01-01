'use strict';

const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

exports.sendNotification = functions.database.ref('/reports/{report}').onUpdate(event => {
    const report = event.params.report;

    const oldStatus = event.data.previous.val().status;
    const newStatus = event.data.val().status;
    const userId = event.data.previous.val().userId;

    if (!oldStatus || !newStatus || oldStatus == newStatus) {
        return;
    }

    console.log('Report ', report, 'has changed. from: ', oldStatus, "to: ", newStatus, 'User: ', userId);

    // Notification details.
    const payload = {
        data: {
            title: 'סטטוס הדיווח השתנה',
            body: `סטטוס ישן: ${oldStatus}, סטטוס חדש: ${newStatus}`,
        }
    };


    var options = {
        priority: "high",
        timeToLive: 60 * 60 * 24
    };

    // Send notifications to user.
    return admin.messaging().sendToDevice(userId, payload, options);

});