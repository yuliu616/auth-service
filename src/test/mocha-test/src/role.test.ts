import { expect } from 'chai';
import { default as axios } from 'axios';

import {
  apiBaseUrl,
  base64Pattern,
  dateTimeAndMsNoZonePattern,
  dateTimeNoZonePattern,
  localDatePattern,
  numberOnlyPattern,
  generalLettersPattern,
} from './common.test';

// access token of ROOT_ADMIN that wont expired before year 2030.
let access_token = 'eyJhbGciOiJFUzI1NksifQ.eyJpc3MiOiJjb20ueXUiLCJyb2xlIjpbIlJPT1RfQURNSU4iXSwiZXhwIjoxOTI1ODA3MTYxLCJ1c2VybmFtZSI6InVzZXIxMDAxIn0.zo7FhCEVhc5A8gBMDSmX06FgVtwbvsg5wEc8EJAJNMiksCsAoHEBCuzOrupk3kdcxVUsGg1Ig-nvHkQ5m7O-2g';
let auth_headers = {
  authorization: `Bearer ${access_token}`,
};

beforeEach(async function(){
  return axios.post(`${apiBaseUrl}/debug/deleteAllTesterUsers`);
});

describe('role', function(){

  it('could list roles of user', async function(){
    // query role list of a user
    let res = await axios.get(`${apiBaseUrl}/role/77001/list`, {
      headers: {
        authorization: `Bearer ${access_token}`,
      }
    });
    expect(res.data).is.an('array');
    expect(res.data).to.have.length(1);
    expect(res.data[0]).eq('ROOT_ADMIN');
  });

  it('support grant/ungrant role to/from user', async function(){
    // query role list of a user
    let res = await axios.get(`${apiBaseUrl}/role/77002/list`, {
      headers: auth_headers
    });
    expect(res.data).is.an('array');
    expect(res.data).to.have.length(1);
    expect(res.data[0]).eq('ROLE_ADMIN');

    // grant role to user
    res = await axios.post(`${apiBaseUrl}/role/77002/bind`, [
      'ROLE_FAKE_1',
      'ROLE_FAKE_2',
    ], { headers: auth_headers });
    expect(res.data).is.an('object');
    expect(res.data.id).eq('77002');
    expect(res.data.username).eq('user1002');
    expect(res.data.passwordHash).to.be.null;
    expect(res.data.active).eq(true);

    // query role list of a user
    res = await axios.get(`${apiBaseUrl}/role/77002/list`, {
      headers: auth_headers
    });
    expect(res.data).is.an('array');
    expect(res.data).to.have.length(3);
    expect(res.data[0]).eq('ROLE_ADMIN');
    expect(res.data[1]).eq('ROLE_FAKE_1');
    expect(res.data[2]).eq('ROLE_FAKE_2');

    // ungrant role from user
    res = await axios.post(`${apiBaseUrl}/role/77002/unbind`, [
      'ROLE_FAKE_1',
      'ROLE_FAKE_2',
    ], { headers: auth_headers });
    expect(res.data).is.an('object');
    expect(res.data.id).eq('77002');
    expect(res.data.username).eq('user1002');
    expect(res.data.passwordHash).to.be.null;
    expect(res.data.active).eq(true);

    // query role list of a user
    res = await axios.get(`${apiBaseUrl}/role/77002/list`, {
      headers: auth_headers
    });
    expect(res.data).is.an('array');
    expect(res.data).to.have.length(1);
    expect(res.data[0]).eq('ROLE_ADMIN');

  });

});
