package scheduler.addition;

import scheduler.models.Task;

import java.util.List;

public class SortByDate {
    public static void sortByDate(List<Task> tasks, boolean direction){
        bubbleSort(tasks,tasks.size(), direction);
    }

    static void bubbleSort(List<Task> tasks,int n,boolean direction) {
        int i, j;
        Task temp;
        boolean swapped;
        for (i = 0; i < n - 1; i++)
        {
            swapped = false;
            for (j = 0; j < n - i - 1; j++) {

                if(direction){
                    if (tasks.get(j).getDate().compareTo(tasks.get(j+1).getDate()) < 0) {
                        temp = tasks.get(j);
                        tasks.set(j,tasks.get(j+1));
                        tasks.set(j+1,temp);
                        swapped = true;
                    }
                }
                else {
                    if (tasks.get(j).getDate().compareTo(tasks.get(j+1).getDate()) > 0) {

                        temp = tasks.get(j);
                        tasks.set(j, tasks.get(j + 1));
                        tasks.set(j + 1, temp);
                        swapped = true;
                    }
                }

            }

            // IF no two elements were
            // swapped by inner loop, then break
            if (!swapped)
                break;
        }
    }
//    static void quickSort(List<Task> tasks, int low, int high){
//        if (tasks.size() == 0)
//            return;//завершить выполнение если длина массива равна 0
//
//        if (low >= high)
//            return;//завершить выполнение если уже нечего делить
//
//        // выбрать опорный элемент
//        int middle = low + (high - low) / 2;
//        int opora = tasks.get(middle).getPriority().value();
//
//        // разделить на подмассивы, который больше и меньше опорного элемента
//        int i = low, j = high;
//        while (i <= j) {
//            while (tasks.get(i).getPriority().value() < opora) {
//                i++;
//            }
//
//            while ( tasks.get(j).getPriority().value() > opora) {
//                j--;
//            }
//
//            if (i <= j) {//меняем местами
//                String temp = tasks.get(i).getPriority().name();
//                tasks.get(i).setPriority(tasks.get(j).getPriority());
//
//                i++;
//                j--;
//            }
//        }
//
//        // вызов рекурсии для сортировки левой и правой части
//        if (low < j)
//            quickSort(tasks, low, j);
//
//        if (high > i)
//            quickSort(tasks, i, high);
//    }
}
