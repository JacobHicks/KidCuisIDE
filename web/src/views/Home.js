import {Link} from 'react-router-dom'
import "./Home.css"

const React = require('react');

export default class Home extends React.Component {
    render() {
        return (
            <html>
            <body>
            <div className="wrapper">
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
                    <img  className="helperImage" src="https://previews.123rf.com/images/stevanovicigor/stevanovicigor1508/stevanovicigor150800150/43924569-tv-damage-bad-sync-tv-channel-rgb-lcd-television-screen-with-static-noise-from-poor-broadcast-signal.jpg"></img>

                    <p className="talk">"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum." WebIDE comes with teacher and student logins that allow teachers to monitor and work with students individually.</p>
                    <div className="buttonDiv">
                        <a href="#section3"><button className="launch" >&#709;</button></a>
                    </div>
                </div>
                <div id="section3" className="section">
                    <h1 className="itemHeading">Ready to work wherever you are.</h1>
                    <h2 className="helperTitle">A full featured cloud-based IDE</h2>
                    <p className="talk">"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum." WebIDE works in the cloud, in any browser, and is always ready for anything from homework assignments to multi-year projects.</p>
                    <div className="buttonDiv">
                        <a href="#section4"><button className="launch" >&#709;</button></a>
                    </div>
                </div>
            </div>
            </div>
            </body>
            </html>
        )
    }
}