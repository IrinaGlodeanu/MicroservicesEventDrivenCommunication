import { Component, OnInit } from '@angular/core';
import { StatisticsService } from './services/statistics.service';
import { Statistic } from './models/Statistic';

@Component({
  selector: 'stats-ui-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  title = 'statistics-ui';

  private statistics: Statistic[];

  constructor(private statisticsService: StatisticsService) {}

  ngOnInit(): void {

    this.statisticsService.getStatistics()
      .subscribe((response) => {
        this.statistics = response;
      });
  }
}
