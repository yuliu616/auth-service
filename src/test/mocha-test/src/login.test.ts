import { expect } from 'chai';
import { default as axios } from 'axios';

import {
  apiBaseUrl,
  base64Pattern,
  dateTimeAndMsNoZonePattern,
  dateTimeNoZonePattern,
  localDatePattern,
  numberOnlyPattern,
} from './common.test';

beforeEach(async function(){
  return axios.post(`${apiBaseUrl}/debug/deleteAllTesterUsers`);
});

describe('login', function(){

  it('pass for newly created user with correct password', async function(){
    let data: any = {
      username: 'tester1001',
      password: 'pass1234',
    };
    let tokenPattern = /^[a-zA-Z0-9\_\-\/\+\=]+[.][a-zA-Z0-9\_\-\/\+\=]+[.][a-zA-Z0-9\_\-\/\+\=]+$/;
    
    // invoke signUp to create user
    let res = await axios.post(`${apiBaseUrl}/signUp`, data);
    expect(res.data).is.an('object');
    expect(res.data.id).to.match(numberOnlyPattern).that.exist;

    data = {
      username: 'tester1001',
      password: 'pass1234',
    };
    // login with the newly created user
    res = await axios.post(`${apiBaseUrl}/login`, data);
    expect(res.data).is.an('object');
    expect(res.data.access_token).to.match(tokenPattern).that.exist;
    expect(res.data.token_type).eq('Bearer');
  });

  it('rejected for newly created user with incorrect password', async function(){
    let data: any = {
      username: 'tester1001',
      password: 'pass1234',
    };
    // invoke signUp to create user
    let res = await axios.post(`${apiBaseUrl}/signUp`, data);
    expect(res.data).is.an('object');
    expect(res.data.id).to.match(numberOnlyPattern).that.exist;

    data = {
      username: 'tester1001',
      password: 'wrong1234',
    };
    // login with the newly created user
    let resError = await axios.post(`${apiBaseUrl}/login`, data).then(res=>{
      return 'endpoint reject expected';
    }).catch(err=>{
      expect(err.response.status).eq(400);
      return err.response.data;
    });
    expect(resError).is.an('object');
    expect(resError.errorCode).eq('AUTHENTICATION_ERROR');
  });

  it('could check about me', async function(){
    let data: any = {
      username: 'tester1001',
      password: 'pass1234',
    };
    let tokenPattern = /^[a-zA-Z0-9\_\-\/\+\=]+[.][a-zA-Z0-9\_\-\/\+\=]+[.][a-zA-Z0-9\_\-\/\+\=]+$/;
    // invoke signUp to create user
    let res = await axios.post(`${apiBaseUrl}/signUp`, data);
    expect(res.data).is.an('object');
    expect(res.data.id).to.match(numberOnlyPattern).that.exist;

    // login with the newly created user
    res = await axios.post(`${apiBaseUrl}/login`, data);
    expect(res.data).is.an('object');
    let access_token = res.data.access_token;

    res = await axios.get(`${apiBaseUrl}/login/aboutMe`, {
      headers: { Authorization: `Bearer ${access_token}` }
    });
    expect(res.data).is.an('object');
    expect(res.data.username).eq('tester1001');
  });

  it('refresh will create new token', async function(){
    let data: any = {
      username: 'tester1001',
      password: 'pass1234',
    };
    let tokenPattern = /^[a-zA-Z0-9\_\-\/\+\=]+[.][a-zA-Z0-9\_\-\/\+\=]+[.][a-zA-Z0-9\_\-\/\+\=]+$/;
    // invoke signUp to create user
    let res = await axios.post(`${apiBaseUrl}/signUp`, data);
    expect(res.data).is.an('object');
    expect(res.data.id).to.match(numberOnlyPattern).that.exist;

    // login with the newly created user
    res = await axios.post(`${apiBaseUrl}/login`, data);
    expect(res.data).is.an('object');
    let access_token = res.data.access_token;

    data = {};
    // invoke refresh token to get new token
    res = await axios.post(`${apiBaseUrl}/login/refreshToken`, data, {
      headers: { Authorization: `Bearer ${access_token}` }
    });
    expect(res.data).is.an('object');
    expect(res.data.access_token).to.not.eq(access_token);
    expect(res.data.access_token).to.match(tokenPattern).that.exist;
    expect(res.data.token_type).eq('Bearer');
    expect(res.data.expires_in).at.least(1);
  });

});

