import React from 'react';
import { Route, BrowserRouter as Router } from 'react-router-dom'
import Home from "./views/Home";

export default (
    <Router>
        <Route exact path='/' component={Home} />
    </Router>
);