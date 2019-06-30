import {Link} from 'react-router-dom'
import "./Home.css"
import { Carousel } from 'antd';
import 'animate.css'

import { version, Button } from "antd";


const React = require('react');
function onChange(a, b, c) {
    console.log(a, b, c);
}


 function lunch() {
   /* const heading = document.getElementById('itemHeading1');
    const helperText = document.getElementById('talk1');
    const helperTitle = document.getElementById('helperTitle1');
    heading.classList.add('animated');
    helperText.classList.add('animated', 'bounceInLeft');
    helperTitle.classList.add('animated', 'bounceInLeft');
*/
}

export default class Home extends React.Component {



    render() {

        return (
            <html>
            <body>
            <div className="wrapper">
            <div className="">
                <h1 className="intro headline test animated fadeInLeftBig 1.5s"><u>WebIDE</u>: The all-new online IDE.</h1>
                <h2 className="subtitle animated fadeInRight 2s">The IDE for students and educators</h2>
               <div className="carouselWrapper animated fadeInUp 3s">
                <Carousel afterChange={onChange()}>
                    <div>
                        <img className="slideImage" src="https://files.slack.com/files-pri/TD2FXNNPN-FKU4NH7LZ/capture.png" alt="Fully-featured IDE"/>
                        <div className="joinDiv">

                            <p className={"caption"}><b>The online IDE for students and learners.</b></p>
                            <a href="/code"><button className="joinButton">Get started!</button></a>
                        </div>
                    </div>
                    <div>
                        <h3>
                            <img className="slideImage" src="https://files.slack.com/files-pri/TD2FXNNPN-FL0EWMFS4/capture.png" alt="A premium cloud-based IDE" />
                            <p className={"caption"}><b>A premium Java IDE</b></p>
                        </h3>
                    </div>
                    <div>
                        <h3>
                            <img className="slideImage" src="https://files.slack.com/files-pri/TD2FXNNPN-FKP2NHT50/capture__1_.png"/>
                            <p className={"caption"}><b>Supports user input and System.in</b></p>
                        </h3>
                    </div>
                </Carousel>
               </div>
                <div className="firstButtonDiv animated bounce infinite slower">
                    <a href="#section2"><button className="launch" onClick={lunch()}>&#8659;</button></a>
                </div>
                <div id="section2" className="section">
                    <h1 className="itemHeading" id="itemHeading1">Education Oriented IDE</h1>
                    <img  className="helperImage left" id="helperImage1"src="https://www.top10spysoftware.com/wp-content/uploads/sm.jpg"></img>

                    <p className="talk" id="talk1">WebIDE will make both adminstrators' and students' lives easier by allowing them to work wherever they are and on any device of their choosing without having to worry about storage or other typical programming and tech hassles. A student will be more likely to complete their assignments is they can even open on their phones. A cloud based IDE makes it incredibly easy for schools to get started with a computer science program and help the next generation live a better life. WebIDE is a free app that is structured around providng students with a way to practice their computer science skills on the go and in the primary language of high school students which is Java.</p>
                </div>
            </div>

            </div>
            </body>
            </html>
        )
    }
}