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

describe('about', function(){

  it('show server info', async function(){
    // query for about
    let res = await axios.get(`${apiBaseUrl}/about`);
    expect(res.data).is.an('object');
    expect(res.data.serviceName).eq('auth-service');
    expect(res.data.apiVersion).eq('1.0');
    expect(res.data.currentDate).to.match(localDatePattern);
    expect(res.data.currentTime).to.match(dateTimeAndMsNoZonePattern);
  });

});
