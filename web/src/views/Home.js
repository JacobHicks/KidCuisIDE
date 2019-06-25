import {Link} from 'react-router-dom'
import "./Home.css"

const React = require('react');

export default class Home extends React.Component {
    render() {
        return (
            <body>
            <Link to="/login">
                <button className="loginButtons">Login</button>
            </Link>

            <Link to="/signup">
                <button className="loginButtons">Signup</button>
            </Link>

            <div>
                <h1 className="intro headline test"><u>WebIDE</u>: The all-new online IDE.</h1>
                <h2 className="subtitle">The IDE for students and educators</h2>
            </div>
            </body>
        )
    }
}