Pod::Spec.new do |s|
  s.name          = 'Amaze'
  s.version       = '1.0.0'
  s.platform      = :ios, '6.0'
  s.framework     = 'GLKit', 'AVFoundation'
  s.requires_arc  = true
  s.ios.deployment_target = '6.0'

  s.license     = 'MIT'
  s.author      = { 'hanqiong' => 'langying.hq@taobao.com' }
  s.source      = { :git => 'http://gitlab.alibaba-inc.com/langying.hq/AmazeSDK.git', :tag => s.version.to_s }
  s.summary     = 'high performance JavaScript WebGL Context.'
  s.homepage    = 'http://gitlab.alibaba-inc.com/langying.hq/Amaze'
  s.description = 'high performance JavaScript WebGL Context.'

  #s.source_files        = ["SwViewCapture/*.swift", "SwViewCapture/SwViewCapture.h"]
  #s.public_header_files = ["SwViewCapture/SwViewCapture.h"]
  #pod 'Amaze', :path=>'../Amaze'
end
