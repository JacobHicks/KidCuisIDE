import React from 'react';
import { Route, BrowserRouter as Router } from 'react-router-dom'
import Home from "./views/Home";
import Code from "./views/Code";

export default (
    <Router>
        <Route exact path='/' component={Home} />
        <Route path='/code' component={Code}/>
    </Router>
);