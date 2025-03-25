import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'timeAgo',
  standalone: true,
})
export class TimeAgoPipe implements PipeTransform {
  transform(value: string | Date): string {
    if (!value) return 'Unknown';

    const listedDate = new Date(value);
    const today = new Date();
    const diffTime = today.getTime() - listedDate.getTime();
    const diffDays = Math.floor(diffTime / (1000 * 60 * 60 * 24));

    return diffDays === 0 ? 'Listed today' : `Listed ${diffDays} day(s) ago`;
  }
}
