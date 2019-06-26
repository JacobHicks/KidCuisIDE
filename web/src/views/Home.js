import {Link} from 'react-router-dom'
import "./Home.css"

const React = require('react');

export default class Home extends React.Component {
    render() {
        return (

            <body>
            <div className="navDiv">
                <Link to="/login">
                    <button className="loginButtons">Login</button>
                </Link>

                <Link to="/signup">
                    <button className="loginButtons">Signup</button>
                </Link>
            </div>
            <div>
                <h1 className="intro headline test"><u>WebIDE</u>: The all-new online IDE.</h1>
                <h2 className="subtitle">The IDE for students and educators</h2>
                <div className="buttonDiv">
                    <a href="#section2"><button className="launch" >&#709;</button></a>
                </div>
                <div id="section2" className="section">
                    <h1 className="itemHeading">School Oriented IDE</h1>
                    <h2 className="helperTitle">With teacher accounts</h2>
                    <p className="talk">WebIDE comes with teacher and student logins that allow teachers to monitor and work with students individually.</p>
                </div>
                <div id="section3" className="section">
                    <h1 className="itemHeading">Ready to work whenever and wherever you are.</h1>
                    <h2 className="helperTitle">A full featured cloud-based IDE</h2>
                    <p className="talk">WebIDE works in the cloud, in any browser, and is always ready for anything from homework assignments to multi-year projects.</p>
                </div>
            </div>
            </body>
        )
    }
}