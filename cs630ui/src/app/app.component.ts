import { Component, OnInit } from '@angular/core';
import { SystemService } from './system.service';
import * as Chart from 'chart.js';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  processor: any;
  cpuCores: number;
  coreInfo: any = [];
  memory: any;

  constructor(private systerService: SystemService) {}  
  ngOnInit(): void {
    this.getProcessor();
    this.getMemory();
    this.update();
  }
  
  osinfo$ = this.systerService.getOsInfo();

  services$ = this.systerService.getServices();

  memory$ = this.systerService.getMemory();
  
  processes$ = this.systerService.getProcesses();

  getProcessor(): void {
    this.systerService.getProcessor().subscribe(
      data => {
        const coreInfos: any = [];
        this.processor = data;
        this.cpuCores = Object.keys(this.processor.cpuLoadPerProcessor).length;
        console.log(this.processor.cpuLoadPerProcessor);
        console.log(Object.keys(this.processor.cpuLoadPerProcessor));
        for (var k in this.processor.cpuLoadPerProcessor) {
          coreInfos.push(
              {name: k, percent: this.processor.cpuLoadPerProcessor[k], frequency: this.processor.frequencyPerCore[k]}
              );
        }
        this.coreInfo = coreInfos;;
        console.log(this.coreInfo);
      }
    );
  }

  getMemory(): void {
    this.systerService.getMemory().subscribe(
      data => {
        this.memory = data;
        this.initializeCharts();
      }
    );
  }

  private initializeCharts(): void {
    let data = [this.memory.available / 1000000, (this.memory.total - this.memory.available) / 1000000, this.memory.total / 1000000];
    let lables = ['Memory Available (MB)', 'Memory Used (MB)', 'Memory Total (MB)'];
    const barChatCtx = (<HTMLCanvasElement>document.getElementById('barChart')).getContext('2d');
    new Chart(barChatCtx, {
      type: 'bar',
      data: {
        labels: lables,
        datasets: [{
          data: data,
          backgroundColor: ["rgba(105,230,189)", "rgba(11,9,195)", "rgba(235,85,120)"],
          borderColor: ["rgba(11,140,95)", "rgba(27,23,81)", "rgba(65,75,4)"],
          borderWidth: 3
        }]
      },
      options: {
        responsive: true,
        title: { display: true, text: [`System Memory as of ${new Date()}`] },
        legend: { display: false },
        scales: {
          yAxes: [{
            ticks: {
              beginAtZero: true
            }
          }]
        }
      }
    });
    const pieChatCtx = (<HTMLCanvasElement>document.getElementById('pieChart')).getContext('2d');
    let data2 = [this.memory.available / 1000000, (this.memory.total - this.memory.available) / 1000000];
    let lables2 = ['Memory Available (MB)', 'Memory Used (MB)'];
    new Chart(pieChatCtx, {
      type: 'pie',
      data: {
        labels: lables2,
        datasets: [{
          data: data2,
          backgroundColor: ["rgba(105,230,189)", "rgba(11,9,195)", "rgba(235,85,120)"],
          borderColor: ["rgba(11,140,95)", "rgba(27,23,81)", "rgba(65,75,4)"],
          borderWidth: 3
        }]
      },
      options: {
        title: { display: true, text: [`System Memory as of ${new Date()}`] },
        legend: { display: true },
        display: true
      }
    });
  }

  private update(): void {
    setInterval(() => {
      this.getProcessor();
      this.initializeCharts();
      this.getMemory();
    }, 10 * 1000);
  }

}
