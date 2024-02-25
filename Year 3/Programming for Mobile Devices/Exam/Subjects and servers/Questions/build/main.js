require("source-map-support").install();
module.exports =
/******/ (function(modules) { // webpackBootstrap
/******/ 	// The module cache
/******/ 	var installedModules = {};
/******/
/******/ 	// The require function
/******/ 	function __webpack_require__(moduleId) {
/******/
/******/ 		// Check if module is in cache
/******/ 		if(installedModules[moduleId])
/******/ 			return installedModules[moduleId].exports;
/******/
/******/ 		// Create a new module (and put it into the cache)
/******/ 		var module = installedModules[moduleId] = {
/******/ 			i: moduleId,
/******/ 			l: false,
/******/ 			exports: {}
/******/ 		};
/******/
/******/ 		// Execute the module function
/******/ 		modules[moduleId].call(module.exports, module, module.exports, __webpack_require__);
/******/
/******/ 		// Flag the module as loaded
/******/ 		module.l = true;
/******/
/******/ 		// Return the exports of the module
/******/ 		return module.exports;
/******/ 	}
/******/
/******/
/******/ 	// expose the modules object (__webpack_modules__)
/******/ 	__webpack_require__.m = modules;
/******/
/******/ 	// expose the module cache
/******/ 	__webpack_require__.c = installedModules;
/******/
/******/ 	// identity function for calling harmony imports with the correct context
/******/ 	__webpack_require__.i = function(value) { return value; };
/******/
/******/ 	// define getter function for harmony exports
/******/ 	__webpack_require__.d = function(exports, name, getter) {
/******/ 		if(!__webpack_require__.o(exports, name)) {
/******/ 			Object.defineProperty(exports, name, {
/******/ 				configurable: false,
/******/ 				enumerable: true,
/******/ 				get: getter
/******/ 			});
/******/ 		}
/******/ 	};
/******/
/******/ 	// getDefaultExport function for compatibility with non-harmony modules
/******/ 	__webpack_require__.n = function(module) {
/******/ 		var getter = module && module.__esModule ?
/******/ 			function getDefault() { return module['default']; } :
/******/ 			function getModuleExports() { return module; };
/******/ 		__webpack_require__.d(getter, 'a', getter);
/******/ 		return getter;
/******/ 	};
/******/
/******/ 	// Object.prototype.hasOwnProperty.call
/******/ 	__webpack_require__.o = function(object, property) { return Object.prototype.hasOwnProperty.call(object, property); };
/******/
/******/ 	// __webpack_public_path__
/******/ 	__webpack_require__.p = "/";
/******/
/******/ 	// Load entry module and return exports
/******/ 	return __webpack_require__(__webpack_require__.s = 7);
/******/ })
/************************************************************************/
/******/ ([
/* 0 */
/***/ function(module, exports, __webpack_require__) {

"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0_C_GIT_University_Year_3_Programming_for_Mobile_Devices_Exam_Subjects_and_servers_Questions_node_modules_babel_runtime_regenerator__ = __webpack_require__(8);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0_C_GIT_University_Year_3_Programming_for_Mobile_Devices_Exam_Subjects_and_servers_Questions_node_modules_babel_runtime_regenerator___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_0_C_GIT_University_Year_3_Programming_for_Mobile_Devices_Exam_Subjects_and_servers_Questions_node_modules_babel_runtime_regenerator__);


var _this = this;

function _toConsumableArray(arr) { if (Array.isArray(arr)) { for (var i = 0, arr2 = Array(arr.length); i < arr.length; i++) { arr2[i] = arr[i]; } return arr2; } else { return Array.from(arr); } }

function _asyncToGenerator(fn) { return function () { var gen = fn.apply(this, arguments); return new Promise(function (resolve, reject) { function step(key, arg) { try { var info = gen[key](arg); var value = info.value; } catch (error) { reject(error); return; } if (info.done) { resolve(value); } else { return Promise.resolve(value).then(function (value) { step("next", value); }, function (err) { step("throw", err); }); } } return step("next"); }); }; }

var Koa = __webpack_require__(2);
var app = new Koa();
var server = __webpack_require__(1).createServer(app.callback());
var WebSocket = __webpack_require__(6);
var wss = new WebSocket.Server({ server: server });
var Router = __webpack_require__(5);
var cors = __webpack_require__(4);
var bodyparser = __webpack_require__(3);

app.use(bodyparser());
app.use(cors());

app.use(function () {
  var _ref = _asyncToGenerator( /*#__PURE__*/__WEBPACK_IMPORTED_MODULE_0_C_GIT_University_Year_3_Programming_for_Mobile_Devices_Exam_Subjects_and_servers_Questions_node_modules_babel_runtime_regenerator___default.a.mark(function _callee(ctx, next) {
    var start, ms;
    return __WEBPACK_IMPORTED_MODULE_0_C_GIT_University_Year_3_Programming_for_Mobile_Devices_Exam_Subjects_and_servers_Questions_node_modules_babel_runtime_regenerator___default.a.wrap(function _callee$(_context) {
      while (1) {
        switch (_context.prev = _context.next) {
          case 0:
            start = new Date();
            _context.next = 3;
            return next();

          case 3:
            ms = new Date() - start;

            console.log(ctx.method + ' ' + ctx.url + ' ' + ctx.response.status + ' - ' + ms + 'ms');

          case 5:
          case 'end':
            return _context.stop();
        }
      }
    }, _callee, this);
  }));

  return function (_x, _x2) {
    return _ref.apply(this, arguments);
  };
}());

function Question(i) {
  this.id = i;
  this.text = i + ' + ' + i + ' = ?';
  this.options = [].concat(_toConsumableArray(Array(4).keys())).map(function (j) {
    return 2 * i - j;
  });
  this.indexCorrectOption = 0;
}

var questions = [].concat(_toConsumableArray(Array(3).keys())).map(function (i) {
  return new Question(i);
});

app.use(function () {
  var _ref2 = _asyncToGenerator( /*#__PURE__*/__WEBPACK_IMPORTED_MODULE_0_C_GIT_University_Year_3_Programming_for_Mobile_Devices_Exam_Subjects_and_servers_Questions_node_modules_babel_runtime_regenerator___default.a.mark(function _callee2(ctx, next) {
    return __WEBPACK_IMPORTED_MODULE_0_C_GIT_University_Year_3_Programming_for_Mobile_Devices_Exam_Subjects_and_servers_Questions_node_modules_babel_runtime_regenerator___default.a.wrap(function _callee2$(_context2) {
      while (1) {
        switch (_context2.prev = _context2.next) {
          case 0:
            _context2.next = 2;
            return new Promise(function (resolve) {
              setTimeout(resolve, 1000);
            });

          case 2:
            _context2.next = 4;
            return next();

          case 4:
          case 'end':
            return _context2.stop();
        }
      }
    }, _callee2, _this);
  }));

  return function (_x3, _x4) {
    return _ref2.apply(this, arguments);
  };
}());

var router = new Router();

router.post('/auth', function (ctx) {
  var id = ctx.request.body.id;
  if (!id) {
    ctx.response.status = 400;
  } else {
    ctx.response.body = { token: id, questionIds: questions.map(function (q) {
        return q.id;
      }) };
    ctx.response.status = 201;
  }
});

router.get('/question/:id', function (ctx) {
  var id = parseInt(ctx.params.id);
  if (id < 0 || id >= questions.length) {
    ctx.response.status = 404;
  } else {
    ctx.response.status = 200;
    ctx.response.body = questions[id];
  }
});

var broadcast = function broadcast(data) {
  return wss.clients.forEach(function (client) {
    if (client.readyState === WebSocket.OPEN) {
      client.send(JSON.stringify({ payload: data }));
    }
  });
};

var nextIndex = questions.length;

setInterval(function () {
  var question = new Question(nextIndex++);
  console.log('broadcast ' + question.text);
  broadcast(question);
}, 10000);

app.use(router.routes());
app.use(router.allowedMethods());

server.listen(3000);

/***/ },
/* 1 */
/***/ function(module, exports) {

module.exports = require("http");

/***/ },
/* 2 */
/***/ function(module, exports) {

module.exports = require("koa");

/***/ },
/* 3 */
/***/ function(module, exports) {

module.exports = require("koa-bodyparser");

/***/ },
/* 4 */
/***/ function(module, exports) {

module.exports = require("koa-cors");

/***/ },
/* 5 */
/***/ function(module, exports) {

module.exports = require("koa-router");

/***/ },
/* 6 */
/***/ function(module, exports) {

module.exports = require("ws");

/***/ },
/* 7 */
/***/ function(module, exports, __webpack_require__) {

module.exports = __webpack_require__(0);


/***/ },
/* 8 */
/***/ function(module, exports, __webpack_require__) {

module.exports = __webpack_require__(9);


/***/ },
/* 9 */
/***/ function(module, exports) {

module.exports = require("regenerator-runtime");

/***/ }
/******/ ]);
//# sourceMappingURL=main.map