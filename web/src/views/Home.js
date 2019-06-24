import {Link} from 'react-router-dom'
import "./Home.css"

const React = require('react');

export default class Home extends React.Component {
    render() {
        return (
            <body>
            <a href="http://localhost:8090/login">
                <button className="loginButtons">Login</button>
            </a>
            <a href="http://localhost:8090/signup">
                <button className="loginButtons">Signup</button>
            </a>

            <div>
                <h1 className="intro headline test"><u>WebIDE</u>: The all-new online IDE.</h1>
                <h2 className="subtitle">The IDE for students and educators</h2>
            </div>

            </body>
        )
    }
}