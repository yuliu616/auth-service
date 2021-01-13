import { default as axios } from 'axios';
import { Colors } from './colors';

let alphanumericPattern = /^[A-Za-z0-9]+$/;
let enumLikePattern = /^[A-Za-z][A-Za-z0-9_]*$/;
let numberOnlyPattern = /^[0-9]+$/;
let generalLettersPattern = /^[a-zA-Z0-9\s\!\@\#\$\%\^\&\*\-\=\_\|\:\;\,\.\?\~\\\/\(\)\[\]\{\}\<\>]+$/;
let localDatePattern = /^[0-9]{4}-[0-9]{2}-[0-9]{2}$/;
let dateTimeNoZonePattern = /^[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}Z$/;
let dateTimeAndMsNoZonePattern = /^[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}[.][0-9]*Z$/;
let base64Pattern = /^[a-zA-Z0-9\/\+\=]+$/;

let apiBaseUrl = 'http://127.0.0.1:8080/api';

let options = {
  // echoReq: 'simple',
  echoReq: false,
  echoRes: false,
  echoErrorRes: false,
};

axios.interceptors.request.use(function(req) {
  if (options.echoReq == 'simple') {
    console.debug(Colors.FgCyan, `     ${req.method.toUpperCase()} ${req.url.replace(apiBaseUrl, '')}`);
  }
  return req;
});

axios.interceptors.response.use(function(res) {
  if (options.echoRes) {
    console.debug(Colors.FgCyan, '       >> response: data = ' 
      + JSON.stringify(res.data));
  }
  return res;
}, function (err) {
  if (options.echoErrorRes) {
    console.error(Colors.FgRed, '       >> response: error[' + err.response.status + '] data = ' 
      + JSON.stringify(err.response.data));
  }
  return Promise.reject(err);
});

export {
  alphanumericPattern,
  enumLikePattern,
  numberOnlyPattern,
  generalLettersPattern,
  localDatePattern,
  dateTimeNoZonePattern,
  dateTimeAndMsNoZonePattern,
  base64Pattern,
  apiBaseUrl,
};
