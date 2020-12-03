import { default as axios } from 'axios';

let numberOnlyPattern = /^[0-9]+$/;
let generalLettersPattern = /^[a-zA-Z0-9\s\!\@\#\$\%\^\&\*\-\=\_\|\:\;\,\.\?\~\\\/\(\)\[\]\{\}\<\>]+$/;
let localDatePattern = /^[0-9]{4}-[0-9]{2}-[0-9]{2}$/;
let dateTimeNoZonePattern = /^[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}Z$/;
let dateTimeAndMsNoZonePattern = /^[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}[.][0-9]*Z$/;
let base64Pattern = /^[a-zA-Z0-9\/\+\=]+$/;

let apiBaseUrl = 'http://127.0.0.1:8080/api';

let options = {
  echoRes: false,
  echoErrorRes: false,
};

axios.interceptors.response.use(function (res) {
  if (options.echoRes) {
    console.log('response.data =', res.data);
  }
  return res;
}, function (err) {
  if (options.echoErrorRes) {
    console.error('error response.data =', err.response.data);
  }
  return Promise.reject(err);
});

export {
  numberOnlyPattern,
  generalLettersPattern,
  localDatePattern,
  dateTimeNoZonePattern,
  dateTimeAndMsNoZonePattern,
  base64Pattern,
  apiBaseUrl,
};
