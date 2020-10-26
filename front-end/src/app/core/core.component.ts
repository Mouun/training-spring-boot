import { Component, OnInit } from '@angular/core';
import { ActivationStart, Router } from '@angular/router';
import { animate, animateChild, query, style, transition, trigger } from '@angular/animations';
import { TokenService } from '../shared/services/token.service';

@Component({
  selector: 'app-core',
  templateUrl: './core.component.html',
  styleUrls: ['./core.component.scss'],
  animations: [
    trigger('sideMenuParentDummyAnimation', [
      transition('* => void', [
        query('@*', [animateChild()], { optional: true })
      ]),
    ]),
    trigger('sideMenuAnimation', [
      transition(
        ':enter',
        [
          style({ transform: 'translateX(-100%)' }),
          animate('300ms ease-in-out',
            style({ transform: 'translateX(0)' }))
        ]
      ),
      transition(
        ':leave',
        [
          style({ transform: 'translateX(0)' }),
          animate('300ms ease-in-out',
            style({ transform: 'translateX(-100%)' }))
        ]
      )
    ]),
    trigger('sideMenuOverlayAnimation', [
      transition(
        ':enter',
        [
          style({ opacity: 0 }),
          animate('300ms linear',
            style({ opacity: 1 }))
        ]
      ),
      transition(
        ':leave',
        [
          style({ opacity: 1 }),
          animate('300ms linear',
            style({ opacity: 0 }))
        ]
      )
    ])
  ]
})

export class CoreComponent implements OnInit {

  public sideMenuOpened = false;
  public pageTitle: string;
  public currentRoute: string;

  constructor(
    private router: Router,
    public tokenService: TokenService
  ) {
  }

  ngOnInit(): void {
    this.currentRoute = this.router.url;
    this.router.events.subscribe(event => {
      if (event instanceof ActivationStart) {
        this.pageTitle = event.snapshot.data.title;
      }
    });
  }

  public navigate(destination: string): void {
    this.router.navigateByUrl(destination);
    this.currentRoute = destination;
    this.toggleSideMenu();
  }

  public toggleSideMenu(): void {
    this.sideMenuOpened = !this.sideMenuOpened;
  }

  public logOut(): void {
    this.tokenService.removeToken();
    this.router.navigateByUrl('/login');
  }
}
