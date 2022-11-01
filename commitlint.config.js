module.exports = {
  'extends': ['@commitlint/config-conventional'],
  'rules': {
    'scope-enum': [1, 'always', [ 'checkstyle', 'config', 'gitlab', 'husky', 'maven', 'package', 'ossrh' ]],
  }
};
