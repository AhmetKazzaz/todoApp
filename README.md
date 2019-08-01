# Todo App 
**This is a task for Huawei's challenge.**
A simple todo app that lets users create a todo list with multiple todo items, each item having basic attributes like name, description, status etc...

#### How to run/install
This Application has no difficulties in installing and running, clone the project, build and run the app directly on your phone or emulator. 
###### 1 Sign Up:
Click on sign up from login page and input your user infortmation.
###### 2 Log In:
After signing up, go ahead and login.
###### 3 TodoLists:
Create a Todo List from Todolits screen.
###### 4 Todo Item: 
Click on your newly created Todo List and create a new Item by entering the fields.
###### 5 Update Todo Items: 
Update TodoItem's status to incomplete/complete and change the previously entered data.
###### 6 Delete Items/Lists: 
You can delete Todolist or a todoItem.
###### 7 Filters/Oreder TodoItems: 
You can order and filter Todo Items based on multiple fields by clickng on filter icon at the toolbar.
###### 8 Export/Share TodoList: 
Export TodoList to Csv format that is readble by excel and many programs. share the exported format by email and save to local storage, by clicking on the green icon on the Todo List


### Structure

**The app has the following packages:**

* `model:` package holds all database/logic related classes.
    -`converters:` contains a date converter class. 
    -`dao:` Data Access Objects classes for Room Database.
    -`entities:` Database Entitiy classes that represent the tables.
    -`relations:` holds a relation between two entities.
    -`repository:` repistory classes that represent the data layer to the application.
* `ui:` package holds ui classes such as custom views and callbacks.
     -`adapter:` has recyclerviews' adapters.
* `utility:` contains classes that deal with file convertion
* `view:` activities/fragments are declared under this package.
      -`activity:` contains the MainActivity.
      -`fragment:` contains all fragments of the app.
* `viewmodel:` holds viewmodel classes that views interact with.





### Library References:
1. RxJava2/RxAndroid: [https://github.com/ReactiveX/RxAndroid]
2. AwesomeValidation: [https://github.com/thyrlian/AwesomeValidation]: simple input validator!
3. Mockito: [https://site.mockito.org/]
4. Apache Commons: [https://commons.apache.org/] for Hashcoding
5. Uses Rx,Room, Databinding, Lifecycler, Navigation Component and Material Libraries.

### License

```
MIT License

Copyright (c) [2019]

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
