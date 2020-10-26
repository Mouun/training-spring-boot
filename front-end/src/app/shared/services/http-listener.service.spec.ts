import { TestBed } from '@angular/core/testing';

import { HttpListenerService } from './http-listener.service';

describe('HttpListenerService', () => {
  let service: HttpListenerService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(HttpListenerService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
