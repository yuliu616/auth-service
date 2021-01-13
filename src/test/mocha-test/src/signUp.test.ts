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

describe('signUp', function(){

  it('could create new user', async function(){
    let data = {
      username: 'tester1001',
      password: 'pass1234',
    };

    // invoke signUp to create user
    let res = await axios.post(`${apiBaseUrl}/signUp`, data);
    expect(res.data).is.an('object');
    expect(res.data.id).to.match(numberOnlyPattern).that.exist;
    expect(res.data.version).at.least(1);
    expect(res.data.creationDate).to.match(dateTimeNoZonePattern).that.exist;
    expect(res.data.lastUpdated).to.match(dateTimeNoZonePattern).that.exist;
    expect(res.data.username).eq('tester1001');
    expect(res.data.passwordHash).to.be.null;
    expect(res.data.active).to.be.true;
  });

});
