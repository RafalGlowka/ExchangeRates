# Exchange

A currency conversion app that allows a user to view a given amount in a given currency converted into other currencies

Example application build on open exchange rates public API as an architecture test case of Kotlin Multiplatform Mobile. 

The purpose of this application is to show usage of some basic libraries and architecture concepts to create one code base application for both Android and iOS. 
* Clean architecture - Currently I resigned from UseCases (quite important part of clean architecture). All of them were 1:1 calls to repositories and do not provide any additional value. By doing so I would like to emphasize the need to rethink the elements of architecture in the context of a particular project. In bigger project use of them may provide additional value in with testing or code reusability. I would use cold-code solution like coroutine Flow in that case.
* MVVM + BusinessFlow
* Compose Multiplatform
* coroutines, flows
* Ktor
* Lottie
* Libres
* Kotest, 
* Turbine, 
* MockEngine for Ktor,
* Detekt

Project was build with AndroidStudio Hedgehog. To build iOS app MacBook with newest XCode is required.

## Screen shots
<table>
        <tr>
            <th>
                <h3>Android</h3>
            </th>
            <th>
                <h3>iOs</h3>
            </th>
        </tr>
        <tr>
            <td colspan="2" align="center">
               Splash
            </td>
        </tr>
        <tr>
            <td> <img src="/documentation/android1.png" width="300"> </td>
            <td> <img src="/documentation/ios1.png" width="300"> </td>
        </tr>
        <tr>
            <td colspan="2" align="center">
                Splash with connection error
            </td>
        </tr>
        <tr>
            <td><img src="/documentation/android3.png" width="300"> </td>
            <td><img src="/documentation/ios3.png" width="300"> </td>
        </tr>
        <tr>
            <td colspan="2" align="center">
                Conversion screen
            </td>
        </tr>
        <tr>
            <td><img src="/documentation/android4.png" width="300"> </td>
            <td><img src="/documentation/ios4.png" width="300"> </td>
        </tr>
        <tr>
            <td colspan="2" align="center">
                Conversion screen for 100 $
            </td>
        </tr>
        <tr>
            <td><img src="/documentation/android5.png" width="300"> </td>
            <td><img src="/documentation/ios5.png" width="300"> </td>
        </tr>
        <tr>
           <td colspan="2" align="center">
                List of available currencies
            </td>
        </tr>
        <tr>
            <td><img src="/documentation/android5.png" width="300"> </td>
            <td><img src="/documentation/ios5.png" width="300"> </td>
        </tr>

</table>


## Test coverage

Code is covered with unit and instrumented tests:
- Compose screens are tested with Instrumented tests (to run them you need emulator or physical android device).
    Tests are placed in shared/androidInstrumentedTest
    Each event emitted by screen is tested there. 
- Validators are tested with unit tests that checks correctness of different inputs
    Tests are placed in shared/androidUnitTest
- ViewModels are tested with unit tests that checks all inputs and view events
    Tests are placed in shared/androidUnitTest
- Flows are tested with unit tests that checks all navigation, screen orders, parameters send between screens and flows. 
    Tests are placed in shared/androidUnitTest
- Usecases and Repositories are tested with unit tests
    Tests are placed in shared/androidUnitTest
- ExchangeAPI is tested with MockEngine
    Tests are placed in shared.androidUnitTest



